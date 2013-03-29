package org.gethydrated.hydra.actors;

import org.gethydrated.hydra.actors.node.ActorNode;

public interface InternalRef extends ActorRef {

    void start();
    void stop();
    void suspend();
    void restart(Throwable cause);
    void resume(Throwable cause);

    void tellSystem(Object o, ActorRef sender);

    /**
     * Gives access to the underlying ActorNode.
     *
     * This cannot be null. There is only one ActorNodeRef per
     * ActorNode.
     *
     * @return ActorNode wrapped by this ActorNodeRef.
     */
    public ActorNode unwrap();
}
