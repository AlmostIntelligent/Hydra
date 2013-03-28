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

    }

    @Override
    public void stop() {
        System.out.println("future stopped");
        parent.tellSystem(new Stopped(getPath()), this);
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

    }

    @Override
    public void unwatch(ActorRef target) {

    }

    @Override
    public void tellSystem(Object o, ActorRef sender) {

    }

    @Override
    public ActorNode unwrap() {
        return null;
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

    }

    @Override
    public void forward(Message m) {

    }

    @Override
    public Future<?> ask(Object o) {
        return null;
    }

    @Override
    public void validate() {

    }
}
