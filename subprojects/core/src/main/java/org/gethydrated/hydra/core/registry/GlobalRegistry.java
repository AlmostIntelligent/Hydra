package org.gethydrated.hydra.core.registry;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.event.*;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.USID;
import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.concurrent.Granted;
import org.gethydrated.hydra.core.concurrent.Lock;
import org.gethydrated.hydra.core.concurrent.Lock.RequestType;
import org.gethydrated.hydra.core.io.network.NodeController;
import org.gethydrated.hydra.core.sid.DefaultSIDFactory;
import org.gethydrated.hydra.core.sid.InternalSID;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Global service registry.
 */
public class GlobalRegistry extends Actor {

    private final NodeController nodeController;

    private final DefaultSIDFactory sidFactory;

    private final InternalHydra hydra;

    private Map<String, USID> registry = new HashMap<>();

    private final Queue<Request> requests = new LinkedList<>();

    private boolean hasLock = false;

    private boolean waitingForLock = false;

    private boolean syncing = false;

    /**
     * Constructor.
     * @param hydra parent Hydra.
     */
    public GlobalRegistry(final InternalHydra hydra) {
        this.sidFactory = (DefaultSIDFactory) hydra.getDefaultSIDFactory();
        this.nodeController = hydra.getNetKernel();
        this.hydra = hydra;
    }

    @Override
    public void onReceive(final Object message) throws Exception {
        if (message instanceof RegisterService
                || message instanceof UnregisterService) {
            acquireLock();
            enqueue(message);
        } else if (message instanceof String) {
            acquireLock();
            enqueue(message);
        } else if (message instanceof Granted) {
            if (((Granted) message).isValid()) {
                hasLock = true;
                waitingForLock = false;
                if (syncing) {
                    sync();
                }
                try {
                    processQueue();
                } finally {
                    releaseLock();
                }
            }
        } else if (message instanceof RegistryState) {
            acceptUpdate(((RegistryState) message).getRegistry());
        } else if (message instanceof Sync) {
            getSender().tell(new RegistryState(registry), getSelf());
        } else if (message instanceof NodeUp) {
            releaseLock();
            acquireLock();
            syncing = true;
        } else if (message instanceof NodeDown) {
            acquireLock();
            enqueue(message);
        } else if (message instanceof ServiceDown) {
            acquireLock();
            enqueue(message);
        }
    }

    private void sync() {
        try {
            Set<UUID> nodes = nodeController.getNodes();
            Set<Future<?>> results = new HashSet<>();
            Set<USID> toKill = new HashSet<>();
            for (UUID node : nodes) {
                ActorRef r = getContext().getActor("/app/nodes/" + nodeController.getID(node));
                results.add(r.ask(new Sync()));
            }
            List<Map<String, USID>> states = new LinkedList<>();
            states.add(registry);
            for (Future<?> r : results) {
                try {
                    RegistryState rs = (RegistryState) r.get(10, TimeUnit.SECONDS);
                    states.add(rs.getRegistry());
                } catch (Exception e) {
                }
            }
            Map<String, USID> newState = new HashMap<>();
            while (!states.isEmpty()) {
                Map<String, USID> s = states.remove(0);
                Iterator<String> it = s.keySet().iterator();
                while (it.hasNext()) {
                    String s1 = it.next();
                    Set<USID> usids = new HashSet<>();
                    usids.add(s.get(s1));
                    for (Map<String, USID> m : states) {
                        USID usid = m.remove(s1);
                        if (usid != null) {
                            usids.add(usid);
                        }
                    }
                    Iterator<USID> it1 = usids.iterator();
                    USID usid = it1.next();
                    Random random = new Random();
                    while (it1.hasNext()) {
                        USID next = it1.next();
                        if (random.nextBoolean()) {
                            toKill.add(usid);
                            usid = next;
                        } else {
                            toKill.add(next);
                        }
                    }
                    newState.put(s1, usid);
                    it.remove();
                }
            }
            registry = newState;
            update();
            for (USID u : toKill) {
                try {
                    hydra.stopService(sidFactory.fromUSID(u));
                } catch (HydraException e) {
                    getLogger(GlobalRegistry.class).warn("{}",
                            e.getMessage(), e);
                }
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            getLogger(GlobalRegistry.class).debug("{}", e.getMessage(), e);
        } finally {
            syncing = false;
        }
    }

    @Override
    public void onStart() {
        getSystem().getEventStream().subscribe(getSelf(), NodeUp.class);
        getSystem().getEventStream().subscribe(getSelf(), NodeDown.class);
    }

    @Override
    public void onStop() {
        getSystem().getEventStream().unsubscribe(getSelf());
    }

    private void acceptUpdate(final Map<String, USID> registry) {
        if (hasLock) {
            getSender()
                    .tell(new RegistryException(
                            "Got registry state while having lock on registry."),
                            getSelf());
        }
        this.registry = new HashMap<>(registry);
        getSender().tell("accepted", getSelf());
    }

    private void processQueue() {
        while (!requests.isEmpty()) {
            final Request r = requests.remove();
            if (r.message instanceof RegisterService) {
                register(((RegisterService) r.message).getSID(),
                        ((RegisterService) r.message).getName(), r.sender);
            } else if (r.message instanceof NodeDown) {
                unregisterAll(((NodeDown) r.message).getUuid());
            } else if (r.message instanceof ServiceDown) {
                unregisterUSID(((ServiceDown) r.message).getSource());
            } else if (r.message instanceof String) {
                retrieve((String) r.message, r.sender);
            } else {
                unregister(((UnregisterService) r.message).getName(), r.sender);
            }
        }
    }

    private void enqueue(final Object message) {
        requests.add(new Request(message, getSender()));
    }

    private void update() throws InterruptedException, ExecutionException,
            TimeoutException {
        final Set<UUID> nodes = getNodes();
        final Set<Future<?>> results = new HashSet<>();
        for (final UUID u : nodes) {
            final ActorRef r = getContext().getActor(
                    "/app/nodes/" + nodeController.getID(u));
            results.add(r.ask(new RegistryState(registry)));
        }
        for (final Future<?> f : results) {
            f.get(10, TimeUnit.SECONDS);
        }
    }

    private void acquireLock() {
        if (!waitingForLock) {
            final ActorRef ref = getContext().getActor("/app/locking");
            ref.tell(new Lock("global", RequestType.LOCK), getSelf());
            waitingForLock = true;
        }
    }

    private void releaseLock() {
        if (hasLock) {
            final ActorRef ref = getContext().getActor("/app/locking");
            ref.tell(new Lock("global", RequestType.UNLOCK), getSelf());
            hasLock = false;
        }
    }

    private void retrieve(final String name, final ActorRef sender) {
        final USID usid = registry.get(name);
        if (usid != null) {
            final SID sid = sidFactory.fromUSID(usid);
            sender.tell(sid, getSelf());
        } else {
            sender.tell(new RegistryException("Name is not in use."), getSelf());
        }
    }

    private void register(final SID sid, final String name,
            final ActorRef sender) {
        if (registry.containsKey(name)) {
            sender.tell(new RegistryException("Name is already in use."),
                    getSelf());
        } else {
            try {
                if (((InternalSID) sid).getRef().isTerminated()) {
                    sender.tell(new RegistryException(
                            "Actor is already stopped."), getSelf());
                }
                registry.put(name, sid.getUSID());
                final SID self = sidFactory.fromActorRef(getSelf());
                sid.tell(new Monitor(self.getUSID(), sid.getUSID()), self);
                update();
                if (sender != null) {
                    sender.tell("ok", getSelf());
                }
            } catch (final Exception e) {
                sender.tell(new RegistryException(e), getSelf());
            }
        }
    }

    private void unregister(final String name, final ActorRef sender) {
        final USID ref = registry.remove(name);
        if (ref != null) {
            try {
                final SID self = sidFactory.fromActorRef(getSelf());
                final SID rem = sidFactory.fromUSID(ref);
                rem.tell(new UnMonitor(self.getUSID()), self);
                update();
                sender.tell("ok", getSelf());
            } catch (final Exception e) {
                sender.tell(new RegistryException(e), getSelf());
            }
        } else {
            sender.tell(new RegistryException("Name is not in use."), getSelf());
        }
    }

    private void unregisterUSID(final USID usid) {
        final Iterator<USID> it = registry.values().iterator();
        while (it.hasNext()) {
            if (it.next().equals(usid)) {
                it.remove();
            }
        }
        try {
            update();
        } catch (final Exception e) {
            getLogger(GlobalRegistry.class).warn("{}", e.getMessage(), e);
        }
    }

    private void unregisterAll(final UUID uuid) {
        final Iterator<USID> it = registry.values().iterator();
        while (it.hasNext()) {
            if (it.next().getNodeId().equals(uuid)) {
                it.remove();
            }
        }
        try {
            update();
        } catch (final Exception e) {
            getLogger(GlobalRegistry.class).warn("{}", e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private Set<UUID> getNodes() throws InterruptedException,
            ExecutionException, TimeoutException {
        final Future<?> f = getContext().getActor("/app/nodes").ask("nodes");
        return (Set<UUID>) f.get(10, TimeUnit.SECONDS);
    }

    /**
     * Request.
     * @author Christian Kulpa
     * @since 0.2.0
     */
    private static class Request {
        private Object message;
        private ActorRef sender;

        public Request(final Object message, final ActorRef sender) {
            this.message = message;
            this.sender = sender;
        }
    }
}
