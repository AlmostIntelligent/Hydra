package org.gethydrated.hydra.core.registry;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.USID;
import org.gethydrated.hydra.core.concurrent.Granted;
import org.gethydrated.hydra.core.concurrent.Lock;
import org.gethydrated.hydra.core.concurrent.Lock.RequestType;
import org.gethydrated.hydra.api.event.Monitor;
import org.gethydrated.hydra.api.event.NodeDown;
import org.gethydrated.hydra.api.event.NodeUp;
import org.gethydrated.hydra.api.event.UnMonitor;
import org.gethydrated.hydra.core.sid.DefaultSIDFactory;
import org.gethydrated.hydra.core.sid.IdMatcher;
import org.gethydrated.hydra.core.sid.InternalSID;
import org.gethydrated.hydra.core.transport.NodeAddress;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class GlobalRegistry extends Actor {

    private final IdMatcher idMatcher;

    private final DefaultSIDFactory sidFactory;

    private Map<String, USID> registry = new HashMap<>();

    private Queue<Request> requests = new LinkedList<>();

    private boolean hasLock = false;

    private boolean waitingForLock = false;

    private boolean syncing = false;

    public GlobalRegistry(DefaultSIDFactory sidFactory, IdMatcher idMatcher) {
        this.sidFactory = sidFactory;
        this.idMatcher = idMatcher;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof RegisterService || message instanceof UnregisterService) {
            acquireLock();
            enqueue(message);
        } else if (message instanceof String) {
            retrieve((String) message);
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
            System.out.println("nodeup:" + ((NodeUp) message).getUuid());
        } else {
            System.out.println(message.toString());
        }
    }

    private void sync() {
        System.out.println("syncing " + getSystem().getClock().getCurrentTime());
        try {
            Random random = new Random();
            Set<UUID> nodes = getNodes();
            System.out.println(registry);
            for(UUID node : nodes) {
                ActorRef r = getContext().getActor("/app/nodes/" + idMatcher.getId(node));
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
                                System.out.println("dropping " + newUSID);
                            } else {
                                registry.put(s.getKey(), newUSID);
                                System.out.println("dropping " + ownUSID);
                            }
                        }
                    }
                }
            }
            System.out.println(registry);
            update();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            System.out.println("sync done " + getSystem().getClock().getCurrentTime());
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
            ActorRef r = getContext().getActor("/app/nodes/" + idMatcher.getId(u));
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

    private void retrieve(String name) {
        USID usid = registry.get(name);
        if(usid != null) {
            SID sid = sidFactory.fromUSID(usid);
            getSender().tell(sid, getSelf());
        } else {
            getSender().tell(new RegistryException("Name is not in use."), getSelf());
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

    private Set<UUID> getNodes() throws InterruptedException, ExecutionException, TimeoutException {
        Future f = getContext().getActor("/app/nodes").ask("nodes");
        Map<UUID, NodeAddress> nodes = (Map<UUID, NodeAddress>) f.get(10, TimeUnit.SECONDS);
        return nodes.keySet();
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
