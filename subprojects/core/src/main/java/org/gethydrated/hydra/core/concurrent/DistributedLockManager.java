package org.gethydrated.hydra.core.concurrent;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.event.NodeDown;
import org.gethydrated.hydra.core.io.network.NodeController;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implements Lamport's Distributed Mutual Exclusion Algorithm.
 */
public class DistributedLockManager extends Actor {

    private final NodeController nodeController;

    private Set<UUID> remainingGranted;

    private Lock holder;

    private boolean enqueued;

    private final HashMap<Lock, ActorRef> localQueue = new HashMap<>();

    private AtomicBoolean currentGranted;

    private final PriorityQueue<LockRequest> queue = new PriorityQueue<>(10,
            new Comparator<LockRequest>() {
                @Override
                public int compare(final LockRequest o1, final LockRequest o2) {
                    if (o1.getTimestamp() == o2.getTimestamp()) {
                        return o1.getNodeId().compareTo(o2.getNodeId());
                    }
                    return ((Long) o1.getTimestamp()).compareTo(o2
                            .getTimestamp());
                }
            });

    /**
     * Constructor.
     * @param nodeController node controller.
     */
    public DistributedLockManager(final NodeController nodeController) {
        this.nodeController = nodeController;
    }

    @Override
    public void onReceive(final Object message) throws Exception {
        try {
            if (message instanceof LockRequest) {
                enqueue((LockRequest) message);
            } else if (message instanceof LockRelease) {
                release((LockRelease) message);
            } else if (message instanceof LockReply) {
                if (remainingGranted != null) {
                    remainingGranted.remove(((LockReply) message).getNodeId());
                    checkGranted();
                }
            } else if (message instanceof Lock) {
                switch (((Lock) message).getType()) {
                case LOCK:
                    enqueueLocal((Lock) message);
                    break;
                case UNLOCK:
                    releaseLocal((Lock) message);
                    break;
                default:
                    break;
                }
            } else if (message instanceof NodeDown) {
                release(new LockRelease(((NodeDown) message).getUuid()));
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void releaseLocal(final Lock lock) {
        if (holder != null && holder.getId().equals(lock.getId())) {
            try {
                final LockRelease rl = new LockRelease(
                        nodeController.getLocal());
                final Set<UUID> nodes = getNodes();
                for (final UUID u : nodes) {
                    final ActorRef r = getContext().getActor(
                            "/app/nodes/" + nodeController.getID(u));
                    r.tell(rl, getSelf());
                }
                holder = null;
                currentGranted.set(false);
                currentGranted = null;
                release(new LockRelease(nodeController.getLocal()));
            } catch (InterruptedException | ExecutionException
                    | TimeoutException e) {
                getSelf().tell(lock, getSender());
            }
        } else if (localQueue.containsKey(lock)) {
            localQueue.remove(lock);
        }
    }

    private void enqueueLocal(final Lock lock) {
        localQueue.put(lock, getSender());
        enqueue(new LockRequest(nodeController.getLocal(), getSystem()
                .getClock().getCurrentTime()));
    }

    private void enqueue(final LockRequest lockRequest) {
        if (lockRequest.getNodeId().equals(nodeController.getLocal())) {
            if (!enqueued && holder == null) {
                try {
                    final Set<UUID> nodes = getNodes();
                    for (final UUID u : nodes) {
                        final ActorRef r = getContext().getActor(
                                "/app/nodes/" + nodeController.getID(u));
                        r.tell(lockRequest, getSelf());
                    }
                    remainingGranted = nodes;
                    queue.add(lockRequest);
                    enqueued = true;
                    checkGranted();
                } catch (InterruptedException | ExecutionException
                        | TimeoutException e) {
                    getSelf().tell(lockRequest, getSender());
                }
            }
        } else {
            queue.add(lockRequest);
            getSender().tell(
                    new LockReply(nodeController.getLocal(), getSystem()
                            .getClock().getCurrentTime()), getSelf());
        }

    }

    private void release(final LockRelease lockRelease) {
        queue.remove(new LockRequest(lockRelease.getNodeId(), 0));
        checkGranted();
    }

    private void checkGranted() {
        if (remainingGranted != null && remainingGranted.isEmpty()
                && !queue.isEmpty()
                && queue.peek().getNodeId().equals(nodeController.getLocal())
                && holder == null) {
            enqueued = false;
            if (!localQueue.isEmpty()) {
                final Entry<Lock, ActorRef> e = localQueue.entrySet()
                        .iterator().next();
                if (e.getValue() != null) {
                    holder = e.getKey();
                    currentGranted = new AtomicBoolean(true);
                    e.getValue().tell(new Granted(currentGranted), getSelf());
                    localQueue.remove(e.getKey());
                } else {
                    release(new LockRelease(nodeController.getLocal()));
                }
            } else {
                release(new LockRelease(nodeController.getLocal()));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Set<UUID> getNodes() throws InterruptedException,
            ExecutionException, TimeoutException {
        final ActorRef nodesRef = getContext().getActor("/app/nodes");
        final Future<?> f = nodesRef.ask("nodes");
        return (Set<UUID>) f.get(10, TimeUnit.SECONDS);
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
