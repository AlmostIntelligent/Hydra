package org.gethydrated.hydra.actors.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.event.EventListener;

import java.util.Map;


/**
 * Event stream implementation.
 */
public class SystemEventStream implements ActorEventStream {

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
    public void publish(Object event) {
        dispatch(event);
    }

    /**
     * Dispatch method.
     *
     * @param event actual event.
     */
    private void dispatch(final Object event) {
        synchronized (listeners) {
            for (Class<?> c : listeners.keySet()) {
                if (c.isInstance(event)) {
                    for (EventListener l : listeners.get(c)) {
                        l.handle(event);
                    }
                }
            }
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
