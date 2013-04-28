package org.gethydrated.hydra.actors.refs;

import java.util.List;
import java.util.concurrent.Future;

import org.gethydrated.hydra.actors.ActorCreator;
import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.node.ActorNode;

/**
 *
 */
public abstract class AbstractMinimalRef implements InternalRef {

    @Override
    public abstract ActorPath getPath();

    @Override
    public abstract InternalRef getParent();

    @Override
    public abstract ActorCreator getCreator();

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void suspend() {
    }

    @Override
    public void restart(final Throwable cause) {
    }

    @Override
    public void resume(final Throwable cause) {
    }

    @Override
    public void tellSystem(final Object o, final ActorRef sender) {
    }

    @Override
    public InternalRef getChild(final String name) {
        return new NullRef();
    }

    @Override
    public InternalRef findActor(final List<String> names) {
        if (names.isEmpty()) {
            return this;
        }
        return new NullRef();
    }

    @Override
    public String getName() {
        return getPath().getName();
    }

    @Override
    public void tell(final Object o, final ActorRef sender) {
    }

    @Override
    public Future<?> ask(final Object o) {
        throw new RuntimeException("Ask is not supported by this ref.");
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public ActorNode unwrap() {
        throw new RuntimeException("Unwrap is not supported by this ref.");
    }
}
