package org.gethydrated.hydra.core.concurrent;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.core.messages.NodeDown;
import org.gethydrated.hydra.core.sid.IdMatcher;
import org.gethydrated.hydra.core.transport.NodeAddress;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Implements Lamport's Distributed Mutual Exclusion Algorithm.
 */
public class DistributedLockManager extends Actor {

    private final ActorRef nodesRef = getContext().getActor("/app/nodes");

    private final IdMatcher idMatcher;

    private Set<UUID> remainingGranted;

    private Lock holder;

    private boolean enqueued;

    private HashMap<Lock, ActorRef> localQueue = new HashMap<>();

    private PriorityQueue<LockRequest> queue = new PriorityQueue<>(10, new Comparator<LockRequest>() {
        @Override
        public int compare(LockRequest o1, LockRequest o2) {
            return ((Long)o1.getTimestamp()).compareTo(o2.getTimestamp());
        }
    });

    public DistributedLockManager(IdMatcher idMatcher) {
        this.idMatcher = idMatcher;
    }

    @Override
    public void onReceive(Object message) throws Exception {

        if(message instanceof LockRequest) {
            enqueue((LockRequest) message);
        } else if(message instanceof LockRelease) {
            release((LockRelease) message);
        } else if (message instanceof LockReply) {
            if(enqueued) {
                remainingGranted.remove(((LockReply) message).getNodeId());
                checkGranted();
            }
        } else if(message instanceof Lock) {
            switch (((Lock) message).getType()) {
                case LOCK:
                    enqueueLocal((Lock) message);
                    break;
                case UNLOCK:
                    releaseLocal((Lock) message);
                    break;
            }
        } else if(message instanceof NodeDown) {
            release(new LockRelease(((NodeDown) message).getUuid()));
        }
    }

    private void releaseLocal(Lock lock) {
        if(holder != null && holder.getId().equals(lock.getId())) {
            try {
                LockRelease rl = new LockRelease(idMatcher.getLocal());
                Set<UUID> nodes = getNodes();
                for(UUID u : nodes) {
                    ActorRef r = getContext().getActor("/app/nodes/" + idMatcher.getId(u));
                    r.tell(rl, getSelf());
                }
                holder = null;
                release(new LockRelease(idMatcher.getLocal()));
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                getSelf().tell(lock, getSender());
            }
        } else if (localQueue.containsKey(lock)) {
            localQueue.remove(lock);
        }
    }

    private void enqueueLocal(Lock lock) {
        localQueue.put(lock, getSender());
        enqueue(new LockRequest(idMatcher.getLocal(),getSystem().getClock().getCurrentTime()));
    }

    private void enqueue(LockRequest lockRequest) {
        if(lockRequest.getNodeId().equals(idMatcher.getLocal())) {
            if(!enqueued && holder == null) {
                try {
                    Set<UUID> nodes = getNodes();
                    for(UUID u : nodes) {
                        ActorRef r = getContext().getActor("/app/nodes/" + idMatcher.getId(u));
                        r.tell(lockRequest, getSelf());
                    }
                    remainingGranted = nodes;
                    queue.add(lockRequest);
                    enqueued = true;
                    checkGranted();
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    getSelf().tell(lockRequest, getSender());
                }
            }
        } else {
            queue.add(lockRequest);
            getSender().tell(new LockReply(idMatcher.getLocal(), getSystem().getClock().getCurrentTime()), getSelf());
        }

    }

    private void release(LockRelease lockRelease) {
        queue.remove(new LockRequest(lockRelease.getNodeId(),0));
        checkGranted();
    }

    private void checkGranted() {
        if(remainingGranted.isEmpty() && !queue.isEmpty() && queue.peek().getNodeId().equals(idMatcher.getLocal()) && holder == null) {
            enqueued = false;
            if(!localQueue.isEmpty()) {
                Entry<Lock, ActorRef> e = localQueue.entrySet().iterator().next();
                if(e.getValue() != null) {
                    holder = e.getKey();
                    e.getValue().tell("granted", getSelf());
                    localQueue.remove(e.getKey());
                } else {
                    release(new LockRelease(idMatcher.getLocal()));
                }
            } else {
                release(new LockRelease(idMatcher.getLocal()));
            }
        }
    }

    private Set<UUID> getNodes() throws InterruptedException, ExecutionException, TimeoutException {
        Future f = nodesRef.ask("nodes");
        Map<UUID, NodeAddress> nodes = (Map<UUID, NodeAddress>) f.get(10, TimeUnit.SECONDS);
        return nodes.keySet();
    }

    @Override
    public void onStart() {
        getSystem().getEventStream().subscribe(getSelf(), NodeDown.class);
    }

    @Override
    public void onStop() {
        getSystem().getEventStream().unsubscribe(getSelf());
    }
}
