package org.gethydrated.hydra.core.coordinator;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.core.coordinator.Coordinator.Callback;
import org.gethydrated.hydra.core.messages.*;
import org.gethydrated.hydra.core.sid.IdMatcher;
import org.gethydrated.hydra.core.transport.NodeAddress;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 
 * @author Christian Kulpa
 *
 */
public class CoordinatorActor extends Actor {

    private CoordinatorHolder<Lock> coordinator;

    private UUID localId;
    private IdMatcher idMatcher;

    public CoordinatorActor(final IdMatcher idMatcher) {
        coordinator = new CoordinatorHolder<>(idMatcher.getLocal(), new Callback<UUID>() {
            @Override
            public void call(UUID obj) {
                ActorRef ref = getContext().getActor("/app/nodes/"+idMatcher.getId(obj));
                ref.tell(new GlobalLockGranted(), getSelf());
            }
        });
        localId = idMatcher.getLocal();
        this.idMatcher = idMatcher;
        coordinator.setGrantedCallback(new Callback<Lock>() {
            @Override
            public void call(Lock obj) {
                obj.ref.tell("granted", getSelf());
            }
        });
        coordinator.setReleasedCallback(new Callback<Lock>() {
            @Override
            public void call(Lock obj) {
                obj.ref.tell("released", getSelf());
            }
        });
    }

    @Override
    public void onReceive(Object message) throws Exception {
        try {
        if (message instanceof NodeUp) {
            System.out.println("coordinator: node up - start election");
            elect();
        } else if(message instanceof NodeDown) {
            System.out.println("coordinator: node down");
            elect();
        } else if(message instanceof Election) {
            System.out.println("coordinator: election");
            elect();
        } else if(message instanceof NewCoordinator) {
            System.out.println("new coordinator");
            newCoordinator((NewCoordinator)message);
        } else if(message instanceof GlobalLockRequest) {
            System.out.println("global lock request "+((GlobalLockRequest) message).getNodeId());
            coordinator.acquireLock(((GlobalLockRequest) message).getNodeId());
        } else if(message instanceof GlobalLockRelease) {
            System.out.println("global lock release " + ((GlobalLockRelease) message).getNodeId());
            coordinator.releaseLock(((GlobalLockRelease) message).getNodeId());
        } else if (message instanceof GlobalLockGranted) {
            coordinator.grantedLock();
        } else if(message instanceof LockRequest) {
            coordinator.acquireLock(new Lock(((LockRequest) message).getSid(), getSender()));
        } else if(message instanceof LockRelease) {
            coordinator.releaseLock(new Lock(((LockRelease) message).getSid(), getSender()));
        }
        }catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void newCoordinator(final NewCoordinator newCoordinator) {
        if (newCoordinator.getNodeId().equals(idMatcher.getLocal())) {
            coordinator.makeLocal(new Callback<UUID>() {
                @Override
                public void call(UUID obj) {
                    ActorRef ref = getContext().getActor("/app/nodes/"+idMatcher.getId(obj));
                    ref.tell(new GlobalLockGranted(), getSelf());
                }
            });
        } else {
            coordinator.makeProxy(new Callback<UUID>() {
                @Override
                public void call(UUID obj) {
                    ActorRef ref = getContext().getActor("/app/nodes/" + idMatcher.getId(newCoordinator.getNodeId()));
                    ref.tell(new GlobalLockRequest(obj), getSelf());
                }
            }, new Callback<UUID>() {
                @Override
                public void call(UUID obj) {
                    ActorRef ref = getContext().getActor("/app/nodes/" + idMatcher.getId(newCoordinator.getNodeId()));
                    ref.tell(new GlobalLockRelease(obj), getSelf());
                }
            });
        }

    }

    private void elect() {
        ActorRef ref = getContext().getActor("/app/nodes");
        Future f = ref.ask("nodes");
        try {
            Map<UUID, NodeAddress> res = (Map<UUID, NodeAddress>) f.get(10, TimeUnit.SECONDS);
            boolean self = true;
            for(UUID u : res.keySet()) {
                if(u.compareTo(localId) > 0) {
                    self = false;
                    ref = getContext().getActor("/app/nodes/"+idMatcher.getId(u));
                    ref.tell(new Election(), getSelf());
                }
            }
            if(self) {
                for(UUID u : res.keySet()) {
                    ref = getContext().getActor("/app/nodes/"+idMatcher.getId(u));
                    ref.tell(new NewCoordinator(idMatcher.getLocal()), getSelf());
                }
                if(!coordinator.isLocal()) {
                    coordinator.makeLocal(new Callback<UUID>() {
                        @Override
                        public void call(UUID obj) {
                            ActorRef ref = getContext().getActor("/app/nodes/"+idMatcher.getId(obj));
                            ref.tell(new GlobalLockGranted(), getSelf());
                        }
                    });
                }
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            getLogger(CoordinatorActor.class).warn(e.getMessage(), e);
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

    private static class Lock {
        private final SID sid;
        private final ActorRef ref;

        Lock(SID sid, ActorRef ref) {
            this.sid = sid;
            this.ref = ref;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Lock lock = (Lock) o;

            if (sid != null ? !sid.equals(lock.sid) : lock.sid != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return sid != null ? sid.hashCode() : 0;
        }
    }
}
