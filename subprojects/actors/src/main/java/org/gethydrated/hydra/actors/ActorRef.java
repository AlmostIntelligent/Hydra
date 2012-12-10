package org.gethydrated.hydra.actors;

import org.gethydrated.hydra.actors.mailbox.Message;

import java.util.concurrent.Future;

/**
 * @author Christian Kulpa
 */
public interface ActorRef {

    /**
     * Returns the name of the actor this reference points to.
     * @return Actor name.
     */
    String getName();

    /**
     *
     * @return actor address.
     */
    ActorURI getAddress();

    /**
     *
     * @param o Message object.
     * @param sender Target actor reference.
     */
    void tell(Object o, ActorRef sender);

    /**
     *
     * @param m Actual message.
     */
    void forward(Message m);

    /**
     *
     * @param o Message object.
     * @param ref Target actor reference.
     * @return Future object.
     */
    Future<?> ask(Object o, ActorRef ref);

}
