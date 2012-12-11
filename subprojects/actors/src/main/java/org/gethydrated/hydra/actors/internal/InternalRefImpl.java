package org.gethydrated.hydra.actors.internal;

import org.gethydrated.hydra.actors.ActorFactory;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.actors.ActorURI;
import org.gethydrated.hydra.actors.SystemMessages.*;
import org.gethydrated.hydra.actors.dispatch.Dispatcher;
import org.gethydrated.hydra.actors.mailbox.Message;
import org.gethydrated.hydra.actors.node.ActorNode;

import java.util.concurrent.Future;

public class InternalRefImpl implements InternalRef {

    private ActorNode actorNode;

    public InternalRefImpl(String name, ActorFactory actorFactory, InternalRef parent, ActorSystem actorSystem, Dispatcher dispatcher) {
        this.actorNode = new ActorNode(name, actorFactory, parent, this, actorSystem, dispatcher  );
    }

    @Override
    public void start() {
        actorNode.start();
    }

    @Override
    public void stop() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void restart() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void pause() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void resume() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void tellSystem(Object o) {
        actorNode.getMailbox().offerSystem(new Message(o, null));
    }

    @Override
    public ActorNode unwrap() {
        return actorNode;
    }

    @Override
    public boolean isTerminated() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ActorURI getAddress() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void tell(Object o, ActorRef sender) {
        actorNode.getMailbox().offer(new Message(o, sender));
    }

    @Override
    public void forward(Message m) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Future<?> ask(Object o, ActorRef ref) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
