package org.gethydrated.hydra.actors.internal;

import org.gethydrated.hydra.actors.node.ActorNode;

/**
 * Wraps an ActorNode into a Reference. There can
 * only be one NodeRef per ActorNode.
 */
public interface NodeRef extends InternalRef {

    /**
     * Gives access to the underlying ActorNode.
     *
     * This cannot be null. There is only one NodeRef per
     * ActorNode.
     *
     * @return ActorNode wrapped by this NodeRef.
     */
    public ActorNode unwrap();
}
