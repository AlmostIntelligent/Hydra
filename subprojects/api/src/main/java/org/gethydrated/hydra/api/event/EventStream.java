package org.gethydrated.hydra.api.event;

import org.gethydrated.hydra.api.event.EventListener;

/**
 * Event steam.
 */
public interface EventStream {



    /**
     * Subscribe to a topic.
     * @param subscriber Subscriber as event listener.
     * @param classifier topic.
     * @return true, if not already subscribed to topic.
     */
    boolean subscribe(EventListener subscriber, Class<?> classifier);


    /**
     * Unsubscribe from a topic.
     * @param subscriber Subscriber as Actor ref.
     * @param classifier topic.
     * @return true, if the subscriber was registered to the topic.
     */
    boolean unsubscribe(EventListener subscriber, Class<?> classifier);


    /**
     * Unsubscribe from all topics.
     * @param subscriber Subscriber as Actor ref.
     * @return true, if the subscriber was registered to a topic.
     */
    boolean unsubscribe(EventListener subscriber);

    /**
     * Publish an object in the event stream.
     * @param event Event object.
     * @return true if published successfully.
     */
    boolean publish(Object event);
}