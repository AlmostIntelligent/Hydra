package org.gethydrated.hydra.actors.refs;

import org.gethydrated.hydra.actors.ActorCreator;
import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.node.ActorNode;

import java.util.List;
import java.util.concurrent.Future;

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
    public void start() { }

    @Override
    public void stop() { }

    @Override
    public void suspend() { }

    @Override
    public void restart(Throwable cause) { }

    @Override
    public void resume(Throwable cause) { }

    @Override
    public void tellSystem(Object o, ActorRef sender) { }

    @Override
    public InternalRef getChild(String name) {
        return new NullRef();
    }

    @Override
    public InternalRef findActor(List<String> names) {
        if(names.isEmpty()) {
            return this;
        }
        return new NullRef();
    }

    @Override
    public String getName() {
        return getPath().getName();
    }

    @Override
    public void tell(Object o, ActorRef sender) { }

    @Override
    public Future<?> ask(Object o) {
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
