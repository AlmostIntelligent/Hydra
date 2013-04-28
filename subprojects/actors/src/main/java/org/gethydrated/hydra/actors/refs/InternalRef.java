package org.gethydrated.hydra.actors.refs;

import java.util.List;

import org.gethydrated.hydra.actors.ActorCreator;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.node.ActorNode;

/**
 * Internal actor reference.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 */
public interface InternalRef extends ActorRef {

    /**
     * Starts the actor.
     */
    void start();

    /**
     * Stops the actor.
     */
    void stop();
    
    /**
     * Suspends the actor.
     */
    void suspend();

    /**
     * Restarts the actor.
     * @param cause cause of the restart.
     */
    void restart(Throwable cause);

    /**
     * Resumes the actor.
     * @param cause cause of the suspension.
     */
    void resume(Throwable cause);

    /**
     * Sends a system message to the actor.
     * @param o Message
     * @param sender Message source actor.
     */
    void tellSystem(Object o, ActorRef sender);

    /**
     * Gets the child with a given name.
     * @param name child name. 
     * @return actor ref of the searched child.
     */
    InternalRef getChild(String name);

    /**
     * Searches for a child actor.
     * @param names Stack of child names.
     * @return actor ref of the searched actor.
     */
    InternalRef findActor(List<String> names);

    /**
     * Returns the parent actor.
     * @return parent actor.
     */
    InternalRef getParent();

    /**
     * Returns the temporary actor creator.
     * @return actor creator.
     */
    ActorCreator getCreator();

    /**
     * Returns the underlying actor node.
     * @return Actor node.
     */
    ActorNode unwrap();
}
