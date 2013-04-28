package org.gethydrated.hydra.actors.event;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.event.EventStream;

/**
 *
 */
public interface ActorEventStream extends EventStream {

    /**
     * Subscribe to a topic.
     * 
     * @param subscriber
     *            Subscriber as actor ref.
     * @param classifier
     *            topic.
     * @return true, if not already subscribed to topic.
     */
    boolean subscribe(ActorRef subscriber, Class<?> classifier);

    /**
     * Unsubscribe from a topic.
     * 
     * @param subscriber
     *            Subscriber as Actor ref.
     * @param classifier
     *            topic.
     * @return true, if the subscriber was registered to the topic.
     */
    boolean unsubscribe(ActorRef subscriber, Class<?> classifier);

    /**
     * Unsubscribe from all topics.
     * 
     * @param subscriber
     *            Subscriber as Actor ref.
     * @return true, if the subscriber was registered to a topic.
     */
    boolean unsubscribe(ActorRef subscriber);

}
