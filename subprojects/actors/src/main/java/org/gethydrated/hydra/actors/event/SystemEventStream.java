package org.gethydrated.hydra.actors.event;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.event.EventListener;
import org.gethydrated.hydra.api.event.EventStream;

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
     * Event listeners multi bag.
     */
    private final Map<Class<?>, List<EventListener>> listeners = new ConcurrentHashMap<>();

    @Override
    public final boolean subscribe(final ActorRef subscriber, final Class<?> classifier) {
        return subscribe(new EventListener() {
            @Override
            public void handle(Object event) {
                subscriber.tell(event, null);
            }
        }, classifier);
    }

    @Override
    public final boolean subscribe(final EventListener subscriber, final Class<?> classifier) {
        List<EventListener> l = listeners.get(classifier);
        if (l == null) {
            List<EventListener> li = new LinkedList<>();
            li.add(subscriber);
            listeners.put(classifier, li);
            return true;
        } else {
            if (l.contains(subscriber)) {
                return false;
            } else {
                l.add(subscriber);
                return true;
            }
        }
    }

    @Override
    public final boolean unsubscribe(final ActorRef subscriber, final Class<?> classifier) {
        //TODO:
        return false;
    }

    @Override
    public final boolean unsubscribe(final EventListener subscriber, final Class<?> classifier) {
        //TODO:
        return false;
    }

    @Override
    public final boolean unsubscribe(final ActorRef subscriber) {
        //TODO:
        return false;
    }

    @Override
    public final boolean unsubscribe(final EventListener subscriber) {
        //TODO:
        return false;
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
        for (Class<?> c : listeners.keySet()) {
            if (c.isInstance(event)) {
                for (EventListener l : listeners.get(c)) {
                    l.handle(event);
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
}
