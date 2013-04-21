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
 *
 */
public class GlobalRegistry extends Actor {

    private final NodeController nodeController;

    private final DefaultSIDFactory sidFactory;

    private final InternalHydra hydra;

    private Map<String, USID> registry = new HashMap<>();

    private Queue<Request> requests = new LinkedList<>();

    private boolean hasLock = false;

    private boolean waitingForLock = false;

    private boolean syncing = false;

    public GlobalRegistry(final InternalHydra hydra) {
        this.sidFactory = (DefaultSIDFactory) hydra.getDefaultSIDFactory();
        this.nodeController = hydra.getNetKernel();
        this.hydra = hydra;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof RegisterService || message instanceof UnregisterService) {
            acquireLock();
            enqueue(message);
        } else if (message instanceof String) {
            acquireLock();
            enqueue(message);
        } else if (message instanceof Granted) {
            hasLock = true;
            waitingForLock = false;
            if(syncing) {
                sync();
            }
            try {
                processQueue();
            } finally {
                releaseLock();
            }

        } else if (message instanceof RegistryState) {
            acceptUpdate(((RegistryState) message).getRegistry());
        } else if (message instanceof Sync) {
            getSender().tell(new RegistryState(registry), getSelf());
        } else if (message instanceof NodeUp) {
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
            Random random = new Random();
            Set<UUID> nodes = getNodes();
            //We only ask one node, as we expect a consistent state.
            ActorRef r = getContext().getActor("/app/nodes/" + nodeController.getID(nodes.iterator().next()));
            Future f = r.ask(new Sync());
            RegistryState state = (RegistryState) f.get(10, TimeUnit.SECONDS);
            for (Map.Entry<String, USID> s :  state.getRegistry().entrySet()) {
                if(!registry.containsKey(s.getKey())) {
                    registry.put(s.getKey(), s.getValue());
                } else {
                    USID ownUSID = registry.get(s.getKey());
                    USID newUSID = s.getValue();
                    if (!ownUSID.equals(newUSID)) {
                        if (random.nextBoolean()) {
                            registry.put(s.getKey(), ownUSID);
                            try {
                                hydra.stopService(sidFactory.fromUSID(newUSID));
                            } catch (HydraException e) {
                                getLogger(GlobalRegistry.class).warn("{}", e.getMessage(), e);
                            }
                        } else {
                            registry.put(s.getKey(), newUSID);
                            try {
                                hydra.stopService(sidFactory.fromUSID(ownUSID));
                            } catch (HydraException e) {
                                getLogger(GlobalRegistry.class).warn("{}", e.getMessage(), e);
                            }
                        }
                    }
                }
            }
            update();
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

    private void acceptUpdate(Map<String, USID> registry) {
        if(hasLock) {
            getSender().tell(new RegistryException("Got registry state while having lock on registry."), getSelf());
        }
        this.registry = new HashMap<>(registry);
        getSender().tell("accepted", getSelf());
    }

    private void processQueue() {
        while (!requests.isEmpty()) {
            Request r = requests.remove();
            if(r.message instanceof RegisterService) {
                register(((RegisterService) r.message).getSID(), ((RegisterService) r.message).getName(), r.sender);
            } else if (r.message instanceof NodeDown) {
                unregisterAll(((NodeDown)r.message).getUuid());
            } else if (r.message instanceof ServiceDown) {
                unregisterUSID(((ServiceDown)r.message).getUSID());
            } else if (r.message instanceof String) {
                retrieve((String) r.message, r.sender);
            } else {
                unregister(((UnregisterService)r.message).getName(), r.sender);
            }
        }
    }

    private void enqueue(Object message) {
        requests.add(new Request(message, getSender()));
    }

    private void update() throws InterruptedException, ExecutionException, TimeoutException {
        Set<UUID> nodes = getNodes();
        Set<Future<?>> results = new HashSet<>();
        for(UUID u : nodes) {
            ActorRef r = getContext().getActor("/app/nodes/" + nodeController.getID(u));
            results.add(r.ask(new RegistryState(registry)));
        }
        for (Future<?> f : results) {
            f.get(10, TimeUnit.SECONDS);
        }
    }

    private void acquireLock() {
        if(!waitingForLock) {
            ActorRef ref = getContext().getActor("/app/locking");
            ref.tell(new Lock("global", RequestType.LOCK), getSelf());
            waitingForLock = true;
        }
    }

    private void releaseLock() {
        if(hasLock) {
            ActorRef ref = getContext().getActor("/app/locking");
            ref.tell(new Lock("global", RequestType.UNLOCK), getSelf());
            hasLock = false;
        }
    }

    private void retrieve(String name, ActorRef sender) {
        USID usid = registry.get(name);
        if(usid != null) {
            SID sid = sidFactory.fromUSID(usid);
            sender.tell(sid, getSelf());
        } else {
            sender.tell(new RegistryException("Name is not in use."), getSelf());
        }
    }


    private void register(SID sid, String name, ActorRef sender) {
        if(registry.containsKey(name)) {
            sender.tell(new RegistryException("Name is already in use."), getSelf());
        } else {
            try {
                if (((InternalSID) sid).getRef().isTerminated()) {
                    sender.tell(new RegistryException("Actor is already stopped."), getSelf());
                }
                registry.put(name, sid.getUSID());
                SID self = sidFactory.fromActorRef(getSelf());
                sid.tell(new Monitor(self.getUSID()), self);
                update();
                if(sender != null) {
                    sender.tell("ok", getSelf());
                }
            } catch (Exception e) {
                sender.tell(new RegistryException(e), getSelf());
            }
        }
    }

    private void unregister(String name, ActorRef sender) {
        USID ref = registry.remove(name);
        if(ref != null) {
            try {
                SID self = sidFactory.fromActorRef(getSelf());
                SID rem = sidFactory.fromUSID(ref);
                rem.tell(new UnMonitor(self.getUSID()), self);
                update();
                sender.tell("ok", getSelf());
            } catch (Exception e) {
                sender.tell(new RegistryException(e), getSelf());
            }
        } else {
            sender.tell(new RegistryException("Name is not in use."), getSelf());
        }
    }

    private void unregisterUSID(USID usid) {
        Iterator<USID> it = registry.values().iterator();
        while (it.hasNext()) {
            if(it.next().equals(usid)) {
                it.remove();
            }
        }
        try {
            update();
        } catch (Exception e) {
            getLogger(GlobalRegistry.class).warn("{}", e.getMessage(), e);
        }
    }

    private void unregisterAll(UUID uuid) {
        Iterator<USID> it = registry.values().iterator();
        while (it.hasNext()) {
            if(it.next().getNodeId().equals(uuid)) {
                it.remove();
            }
        }
        try {
            update();
        } catch (Exception e) {
            getLogger(GlobalRegistry.class).warn("{}", e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private Set<UUID> getNodes() throws InterruptedException, ExecutionException, TimeoutException {
        Future f = getContext().getActor("/app/nodes").ask("nodes");
        return  (Set<UUID>) f.get(10, TimeUnit.SECONDS);
    }

    private static class Request {
        Object message;
        ActorRef sender;
        public Request(Object message, ActorRef sender) {
            this.message = message;
            this.sender = sender;
        }
    }
}
