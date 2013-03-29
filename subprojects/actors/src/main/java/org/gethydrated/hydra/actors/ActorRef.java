package org.gethydrated.hydra.actors;

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
     * @param o Message object.
     * @return SyncVar object.
     */
    Future<?> ask(Object o);

    /**
     *
     */
    boolean isTerminated();

}
