package org.gethydrated.hydra.actors.actors;

import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.InternalRef;
import org.gethydrated.hydra.actors.SystemMessages.Stopped;
import org.gethydrated.hydra.actors.mailbox.Message;
import org.gethydrated.hydra.actors.node.ActorNode;

import java.util.concurrent.Future;

/**
 *
 */
public class FutureActor implements InternalRef {

    private final InternalRef parent;

    private static final String ACTOR_NAME = "future";

    public FutureActor(InternalRef parent) {
        this.parent = parent;
    }

    @Override
    public void start() {
        throw new RuntimeException("[Start] Don't call me.");
    }

    @Override
    public void stop() {
        parent.tellSystem(new Stopped(getPath()), this);
    }

    @Override
    public void suspend() {

    }

    @Override
    public void restart(Throwable cause) {

    }

    @Override
    public void resume(Throwable cause) {

    }

    @Override
    public void tellSystem(Object o, ActorRef sender) {
        System.out.println(o);
    }

    @Override
    public ActorNode unwrap() {
        throw new RuntimeException("[Unwrap] Don't call me.");
    }

    @Override
    public String getName() {
        return ACTOR_NAME;
    }

    @Override
    public ActorPath getPath() {
        return parent.getPath().createChild(ACTOR_NAME);
    }

    @Override
    public void tell(Object o, ActorRef sender) {
        throw new RuntimeException("[Tell] Don't call me.");
    }

    @Override
    public void forward(Message m) {
        throw new RuntimeException("[Forward] Don't call me.");
    }

    @Override
    public Future<?> ask(Object o) {
        throw new RuntimeException("[Ask] Don't call me.");
    }

    @Override
    public boolean isTerminated() {
        return false;
    }
}
