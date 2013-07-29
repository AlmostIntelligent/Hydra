package org.gethydrated.hydra.core.concurrent;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.event.NodeDown;
import org.gethydrated.hydra.api.event.NodeUp;
import org.gethydrated.hydra.core.io.network.NodeController;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implements Lamport's Distributed Mutual Exclusion Algorithm.
 */
public class DistributedLockManager extends Actor {

    private final NodeController nodeController;

    private Lock holder;

    private AtomicBoolean granted;

    private boolean enqueued;

    private boolean haslock;

    private final HashMap<Lock, ActorRef> localQueue = new HashMap<>();

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
            if (message instanceof Lock) {
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
            }
            if (message instanceof LockGranted) {
                System.out.println("lockgranted");
                processQueue();
                enqueued = false;
                haslock = true;
            }
        } catch (final Throwable t) {
            t.printStackTrace();
        }
    }

    private void releaseLocal(final Lock lock) {
        System.out.println("release local " + enqueued);
        if (lock.equals(holder)) {
            granted.set(false);
            granted = null;
            holder = null;

        }
        System.out.println(localQueue.remove(lock));
        if (localQueue.size() == 0) {
            System.out.println("release global");
            getContext().getActor("/app/coordinator").tell(new LockRelease(nodeController.getLocal()), getSelf());
            enqueued = false;
            haslock = false;
        }
    }

    private void enqueueLocal(final Lock lock) {
        System.out.println("request local " + enqueued);
        localQueue.put(lock, getSender());
        if (!enqueued && !haslock) {
            System.out.println("request global");
            getContext().getActor("/app/coordinator").tell(new LockRequest(nodeController.getLocal(), getSystem().getClock().getCurrentTime()), getSelf());
            enqueued = true;
        }
    }

    private void processQueue() {
        if (holder == null) {
            if (localQueue.size() > 0) {
                Lock l = localQueue.keySet().iterator().next();
                holder = l;
                granted = new AtomicBoolean(true);
                ActorRef ref = localQueue.get(l);
                ref.tell(new Granted(granted), getSelf());
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
