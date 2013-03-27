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
    ActorPath getPath();

    /**
     *
     * @param o Message object.
     * @param sender Target actor reference.
     */
    void tell(Object o, ActorRef sender);

    /**
     *
     * @param m Actual messages.
     */
    void forward(Message m);

    /**
     *
     * @param o Message object.
     * @return FutureImpl object.
     */
    Future<?> ask(Object o);

    /**
     * Tries to validate the current state of
     * the referenced actor.
     * @throws Exception on validation error.
     */
    void validate();

}
