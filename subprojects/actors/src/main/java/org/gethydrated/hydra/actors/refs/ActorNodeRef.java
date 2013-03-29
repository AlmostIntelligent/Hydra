package org.gethydrated.hydra.actors.refs;

import org.gethydrated.hydra.actors.*;
import org.gethydrated.hydra.actors.node.ActorNode;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Wraps an ActorNode into a Reference. There can
 * only be one ActorNodeRef per ActorNode.
 */
public class ActorNodeRef implements InternalRef {

    private final ActorNode actorNode;

    private final ActorPath actorPath;

    public ActorNodeRef(String name, ActorFactory actorFactory, InternalRef parent, ActorCreator creator) {
        this.actorPath = parent.getPath().createChild(name);
        this.actorNode = new ActorNode(this, parent, actorFactory, creator);
    }

    @Override
    public void start() {
        actorNode.start();
    }

    @Override
    public void stop() {
        actorNode.stop();
    }

    @Override
    public void restart(Throwable cause) {
        actorNode.restart(cause);
    }

    @Override
    public void suspend() {
        actorNode.suspend();
    }

    @Override
    public void resume(Throwable cause) {
        actorNode.resume(cause);
    }

    @Override
    public void tellSystem(Object o, ActorRef sender) {
        actorNode.sendSystem(o, sender);
    }

    @Override
    public InternalRef getChild(String name) {
        InternalRef child = actorNode.getChild(name);
        if(child != null) {
        return child;
        }
        throw new RuntimeException("Actor not found:" + getPath().toString() + "/" + name);
    }

    @Override
    public InternalRef findActor(List<String> names) {
        return null;
    }

    @Override
    public InternalRef getParent() {
        return actorNode.getParent();
    }

    @Override
    public ActorCreator getCreator() {
        return actorNode.getCreator();
    }

    @Override
    public ActorNode unwrap() {
        return actorNode;
    }

    @Override
    public String getName() {
        return actorPath.getName();
    }

    @Override
    public ActorPath getPath() {
        return actorPath;
    }

    @Override
    public void tell(Object o, ActorRef sender) {
        actorNode.sendMessage(o,sender);
    }

    @Override
    public Future<?> ask(Object o) {
        SyncVar<?> f = new SyncVar<>();
        FutureRef<?> ref = new FutureRef<>(f, actorNode.getCreator());
        tell(o, ref);
        return f;
    }

    @Override
    public boolean isTerminated() {
        return actorNode.isTerminated();
    }

    @Override
    public String toString() {
        return actorPath.toString();
    }
}
