package org.gethydrated.hydra.actors.event;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.event.EventListener;


/**
 * Event stream implementation.
 */
public class SystemEventStream implements ActorEventStream {

    /**
     * Event object queue.
     */
    private final BlockingQueue<Object> events = new LinkedBlockingQueue<>();

    /**
     * Event stream executor.
     */
    private ExecutorService threadpool;

    /**
     * Event dispatchers.
     */
    private final List<EventDispatcher> dispatchers = new LinkedList<>();

    /**
     * Event listeners multi map.
     */
    private final Multimap<Class<?>, EventListener> listeners = HashMultimap.create();

    @Override
    public final boolean subscribe(final ActorRef subscriber, final Class<?> classifier) {
        return subscribe(new ActorRefListener(subscriber), classifier);
    }

    @Override
    public final boolean subscribe(final EventListener subscriber, final Class<?> classifier) {
        synchronized (listeners) {
            return listeners.put(classifier, subscriber);
        }
    }

    @Override
    public final boolean unsubscribe(final ActorRef subscriber, final Class<?> classifier) {
        return unsubscribe(new ActorRefListener(subscriber), classifier);
    }

    @Override
    public final boolean unsubscribe(final EventListener subscriber, final Class<?> classifier) {
        synchronized (listeners) {
            return listeners.remove(classifier,subscriber);
        }
    }

    @Override
    public final boolean unsubscribe(final ActorRef subscriber) {
        return unsubscribe(new ActorRefListener(subscriber));
    }

    @Override
    public final boolean unsubscribe(final EventListener subscriber) {
        synchronized (listeners) {
            if(!listeners.containsValue(subscriber)) {
                return false;
            }
            for(Map.Entry e: listeners.entries()) {
                if(e.getValue().equals(subscriber)) {
                    listeners.remove(e.getKey(), e.getValue());
                }
            }
        }
        return true;
    }

    @Override
    public final boolean publish(final Object event) {
        return events.offer(event);
    }

    /**
     * Starts the event handling with d dispatchers.
     *
     * @param d Event dispatcher number.
     */
    public void startEventHandling(final int d) {
        if (threadpool == null) {
            threadpool = Executors.newCachedThreadPool();
            for (int i = 0; i < d; i++) {
                EventDispatcher e = new EventDispatcher();
                dispatchers.add(e);
                threadpool.execute(e);
            }
        }
    }

    /**
     * Stops event handling.
     */
    public void stopEventHandling() {
        if (threadpool != null) {
            for (EventDispatcher e : dispatchers) {
                e.stop();
            }
            threadpool.shutdownNow();
            threadpool = null;
        }
    }

    /**
     * Returns if the event stream has published event in the queue.
     *
     * @return true, if there are unhandled event.
     */
    public boolean hasRemainingEvents() {
        return !events.isEmpty();
    }

    /**
     * Drains the event queue into a list.
     *
     * @return remaining event.
     */
    public List<Object> getRemainingEvents() {
        List<Object> l = new LinkedList<>();
        events.drainTo(l);
        return l;
    }

    /**
     * Dispatch method.
     *
     * @param event actual event.
     */
    private void dispatch(final Object event) {
        Set<Class<?>> keyset;
        synchronized (listeners) {
            keyset = listeners.keySet();
        }
        for (Class<?> c : keyset) {
            if (c.isInstance(event)) {
                synchronized (listeners) {
                    for (EventListener l : listeners.get(c)) {
                        l.handle(event);
                    }
                }
            }
        }
    }

    /**
     * Event dispatcher.
     */
    private class EventDispatcher implements Runnable {

        /**
         * Dispatcher status.
         */
        private final AtomicBoolean running = new AtomicBoolean(true);

        @Override
        public void run() {
            while (running.get()) {
                try {
                    Object o = events.take();
                    dispatch(o);
                } catch (InterruptedException e) {
                }
            }
        }

        /**
         * Stops the dispatcher. The dispatcher will
         * exit its loop after finishing the actual event.
         */
        public void stop() {
            running.set(false);
        }
    }

    private class ActorRefListener implements EventListener {

        ActorRef ref;

        ActorRefListener(ActorRef ref) {
            this.ref = ref;
        }

        @Override
        public void handle(Object event) {
            ref.tell(event, null);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ActorRefListener that = (ActorRefListener) o;

            if (!ref.equals(that.ref)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return ref.hashCode();
        }


    }
}
