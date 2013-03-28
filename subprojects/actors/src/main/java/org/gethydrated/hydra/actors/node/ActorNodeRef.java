package org.gethydrated.hydra.actors.node;

import org.gethydrated.hydra.actors.*;
import org.gethydrated.hydra.actors.SystemMessages.Start;
import org.gethydrated.hydra.actors.SystemMessages.Stop;
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
        tellSystem(new Start(), null);
    }

    @Override
    public void stop() {
        tellSystem(new Stop(), null);
    }

    @Override
    public void restart() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void watch(ActorRef target) {
        System.out.println("watch "+target);
    }

    @Override
    public void unwatch(ActorRef target) {
        System.out.println("unwatch "+target);
    }

    @Override
    public void tellSystem(Object o, ActorRef sender) {
        actorNode.getMailbox().offerSystem(new Message(o, sender));
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
        actorNode.getMailbox().offer(new Message(o, sender));
    }

    @Override
    public void forward(Message m) {
        throw new RuntimeException("Not implemented: forward"); //TODO:
    }

    @Override
    public Future<?> ask(Object o) {
        FutureImpl<Object> f = new FutureImpl<>();
        tell(o, f);
        return f;
    }

    @Override
    public void validate() {

    }

    @Override
    public String toString() {
        return actorPath.toString();
    }
}
