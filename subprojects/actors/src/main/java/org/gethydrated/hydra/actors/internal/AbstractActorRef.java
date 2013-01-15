package org.gethydrated.hydra.actors.internal;

import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.FutureImpl;
import org.gethydrated.hydra.actors.mailbox.Mailbox;
import org.gethydrated.hydra.actors.mailbox.Message;

import java.util.concurrent.Future;

/**
 *
 */
public abstract class AbstractActorRef implements ActorRef {

    private final ActorPath actorPath;

    public AbstractActorRef(ActorPath path) {
        actorPath = path;
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
        getMailbox().offer(new Message(o, sender));
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
    public String toString() {
        return actorPath.toString();
    }

    protected abstract Mailbox getMailbox();
}
