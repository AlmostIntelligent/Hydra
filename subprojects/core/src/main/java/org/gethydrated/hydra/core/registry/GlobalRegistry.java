package org.gethydrated.hydra.core.registry;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.USID;
import org.gethydrated.hydra.core.concurrent.Granted;
import org.gethydrated.hydra.core.concurrent.Lock;
import org.gethydrated.hydra.core.concurrent.Lock.RequestType;
import org.gethydrated.hydra.core.messages.Monitor;
import org.gethydrated.hydra.core.messages.UnMonitor;
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
            processQueue();
            releaseLock();
        } else if (message instanceof RegistryState) {
            acceptUpdate(((RegistryState) message).getRegistry());
        }
    }

    private void acceptUpdate(Map<String, USID> registry) {
        if(hasLock) {
            getSender().tell(new IllegalStateException("Got registry state while having lock on registry."), getSelf());
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
        for(UUID u : nodes) {
            ActorRef r = getContext().getActor("/app/nodes/" + idMatcher.getId(u));
            r.tell(new RegistryState(registry), getSelf());
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
