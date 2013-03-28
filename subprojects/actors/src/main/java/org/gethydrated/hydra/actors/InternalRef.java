package org.gethydrated.hydra.actors;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.node.ActorNode;

public interface InternalRef extends ActorRef {

    void start();
    void stop();
    void restart();
    void pause();
    void resume();
    void watch(ActorRef target);
    void unwatch(ActorRef target);

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
