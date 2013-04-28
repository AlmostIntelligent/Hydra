package org.gethydrated.hydra.actors.event;

import java.util.Iterator;
import java.util.Map.Entry;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.event.EventListener;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Event stream implementation.
 */
public class SystemEventStream implements ActorEventStream {

    /**
     * Event listeners multi map.
     */
    private final Multimap<Class<?>, EventListener> listeners = HashMultimap
            .create();

    @Override
    public final boolean subscribe(final ActorRef subscriber,
            final Class<?> classifier) {
        return subscribe(new ActorRefListener(subscriber), classifier);
    }

    @Override
    public final boolean subscribe(final EventListener subscriber,
            final Class<?> classifier) {
        synchronized (listeners) {
            return listeners.put(classifier, subscriber);
        }
    }

    @Override
    public final boolean unsubscribe(final ActorRef subscriber,
            final Class<?> classifier) {
        return unsubscribe(new ActorRefListener(subscriber), classifier);
    }

    @Override
    public final boolean unsubscribe(final EventListener subscriber,
            final Class<?> classifier) {
        synchronized (listeners) {
            return listeners.remove(classifier, subscriber);
        }
    }

    @Override
    public final boolean unsubscribe(final ActorRef subscriber) {
        return unsubscribe(new ActorRefListener(subscriber));
    }

    @Override
    public final boolean unsubscribe(final EventListener subscriber) {
        synchronized (listeners) {
            if (!listeners.containsValue(subscriber)) {
                return false;

            }
            final Iterator<Entry<Class<?>, EventListener>> i = listeners
                    .entries().iterator();
            while (i.hasNext()) {
                if (i.next().getValue().equals(subscriber)) {
                    i.remove();
                }
            }
        }
        return true;
    }

    @Override
    public void publish(final Object event) {
        dispatch(event);
    }

    /**
     * Dispatch method.
     * 
     * @param event
     *            actual event.
     */
    private void dispatch(final Object event) {
        synchronized (listeners) {
            for (final Class<?> c : listeners.keySet()) {
                if (c.isInstance(event)) {
                    for (final EventListener l : listeners.get(c)) {
                        l.handle(event);
                    }
                }
            }
        }

    }
    
    /**
     * Actor ref listener.
     * 
     * @author Christian Kulpa
     * @author Hanno Sternberg
     * @since 0.1.0
     */
    private class ActorRefListener implements EventListener {

        private ActorRef ref;

        ActorRefListener(final ActorRef ref) {
            this.ref = ref;
        }

        @Override
        public void handle(final Object event) {
            ref.tell(event, null);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final ActorRefListener that = (ActorRefListener) o;

            if (!ref.equals(that.ref)) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            return ref.hashCode();
        }

    }
}
