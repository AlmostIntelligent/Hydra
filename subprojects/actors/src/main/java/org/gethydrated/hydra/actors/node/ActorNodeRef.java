package org.gethydrated.hydra.actors.node;

import org.gethydrated.hydra.actors.*;
import org.gethydrated.hydra.actors.mailbox.Message;

import java.util.concurrent.Future;

/**
 * Wraps an ActorNode into a Reference. There can
 * only be one ActorNodeRef per ActorNode.
 */
public class ActorNodeRef implements InternalRef {

    private final ActorNode actorNode;

    private final ActorPath actorPath;

    public ActorNodeRef(String name, ActorFactory actorFactory, InternalRef parent, ActorSystem actorSystem) {
        this.actorPath = parent.getPath().createChild(name);
        this.actorNode = new ActorNode(this, parent, actorFactory, actorSystem);
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
    public void forward(Message m) {
        throw new RuntimeException("Not implemented: forward"); //TODO:
    }

    @Override
    public Future<?> ask(Object o) {
        InternalRef ref = (InternalRef)actorNode.getActor("/sys/future");
        ref.tellSystem(actorPath, this);
        FutureImpl<Object> f = new FutureImpl<>();
        tell(o, f);
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
