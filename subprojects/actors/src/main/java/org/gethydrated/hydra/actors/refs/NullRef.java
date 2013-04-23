package org.gethydrated.hydra.actors.refs;

import org.gethydrated.hydra.actors.ActorCreator;
import org.gethydrated.hydra.actors.ActorPath;

/**
 *
 */
public class NullRef extends AbstractMinimalRef {

    @Override
    public ActorPath getPath() {
        return new ActorPath().createChild("null");
    }

    @Override
    public InternalRef getParent() {
        return this;
    }

    @Override
    public ActorCreator getCreator() {
        throw new RuntimeException("Operation not supported.");
    }

    @Override
    public final boolean isTerminated() {
        return true;
    }
}
