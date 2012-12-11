package org.gethydrated.hydra.actors.internal;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.actors.ActorURI;
import org.gethydrated.hydra.actors.logging.LoggingAdapter;
import org.gethydrated.hydra.actors.mailbox.Message;
import org.gethydrated.hydra.actors.node.ActorNode;
import org.slf4j.Logger;

import java.util.concurrent.Future;

public class RootGuardian implements InternalRef {

    private final Logger logger;

    public RootGuardian(ActorSystem actorSystem) {
        logger = new LoggingAdapter(RootGuardian.class, actorSystem);
        logger.info("RootGuardian created.");
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
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
    public void tellSystem(Object o) {

    }

    @Override
    public ActorNode unwrap() {
        return null;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public String getName() {
        return "rootGuardian";
    }

    @Override
    public ActorURI getAddress() {
        return null;
    }

    @Override
    public void tell(Object o, ActorRef sender) {
    }

    @Override
    public void forward(Message m) {
    }

    @Override
    public Future<?> ask(Object o, ActorRef ref) {
        return null;
    }
}
