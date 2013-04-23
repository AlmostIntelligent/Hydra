package org.gethydrated.hydra.core.concurrent;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.event.NodeDown;
import org.gethydrated.hydra.api.event.NodeUp;
import org.gethydrated.hydra.core.io.network.NodeController;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implements Lamport's Distributed Mutual Exclusion Algorithm.
 */
public class DistributedLockManager extends Actor {

    private final Set<UUID> lockableNodes = new HashSet<>();

    private final NodeController nodeController;

    private Set<UUID> remainingGranted;

    private Lock holder;

    private boolean enqueued;

    private final HashMap<Lock, ActorRef> localQueue = new HashMap<>();

    private AtomicBoolean currentGranted;

    private LockRequest currentLockRequest;

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
                lockableNodes.remove(((NodeDown) message).getUuid());
            } else if (message instanceof NodeUp) {
                if (currentLockRequest != null) {
                    final LockRelease rl = new LockRelease(
                            nodeController.getLocal());
                    queue.remove(rl);
                    for (UUID u : lockableNodes) {
                        final ActorRef r = getContext().getActor(
                                "/app/nodes/" + nodeController.getID(u));
                        r.tell(rl, getSelf());
                    }
                }
                lockableNodes.add(((NodeUp) message).getUuid());
                if (currentLockRequest != null) {
                    for (UUID u : lockableNodes) {
                        final ActorRef r = getContext().getActor(
                                "/app/nodes/" + nodeController.getID(u));
                        r.tell(currentLockRequest, getSelf());
                    }
                    queue.add(currentLockRequest);
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void releaseLocal(final Lock lock) {
        if (holder != null && holder.getId().equals(lock.getId())) {
            final LockRelease rl = new LockRelease(
                    nodeController.getLocal());
            for (final UUID u : lockableNodes) {
                final ActorRef r = getContext().getActor(
                        "/app/nodes/" + nodeController.getID(u));
                r.tell(rl, getSelf());
            }
            holder = null;
            currentGranted.set(false);
            currentGranted = null;
            release(new LockRelease(nodeController.getLocal()));
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
                for (final UUID u : lockableNodes) {
                    final ActorRef r = getContext().getActor(
                            "/app/nodes/" + nodeController.getID(u));
                    r.tell(lockRequest, getSelf());
                }
                remainingGranted = new HashSet<>(lockableNodes);
                queue.add(lockRequest);
                currentLockRequest = lockRequest;
                enqueued = true;
                checkGranted();
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
                    currentLockRequest = null;
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

    @Override
    public void onStart() {
        getSystem().getEventStream().subscribe(getSelf(), NodeDown.class);
        getSystem().getEventStream().subscribe(getSelf(), NodeUp.class);
    }

    @Override
    public void onStop() {
        getSystem().getEventStream().unsubscribe(getSelf());
    }
}
