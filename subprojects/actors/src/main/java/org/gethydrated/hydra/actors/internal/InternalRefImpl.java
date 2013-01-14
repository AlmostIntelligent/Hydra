package org.gethydrated.hydra.actors.internal;

import org.gethydrated.hydra.actors.*;
import org.gethydrated.hydra.actors.SystemMessages.*;
import org.gethydrated.hydra.actors.dispatch.Dispatcher;
import org.gethydrated.hydra.actors.mailbox.Message;
import org.gethydrated.hydra.actors.node.ActorNode;

import java.util.concurrent.Future;

public class InternalRefImpl implements InternalRef {

    private final ActorNode actorNode;

    private final InternalRef parent;

    public InternalRefImpl(String name, ActorFactory actorFactory, InternalRef parent, ActorSystem actorSystem, Dispatcher dispatcher) {
        this.parent = parent;
        this.actorNode = new ActorNode(name, actorFactory, this, actorSystem, dispatcher  );
    }

    @Override
    public void start() {
        actorNode.start();
    }

    @Override
    public void stop() {
        tellSystem(new Stop(), null);
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
    public void tellSystem(Object o, ActorRef ref) {
        actorNode.getMailbox().offerSystem(new Message(o, ref));
    }

    @Override
    public InternalRef parent() {
        return parent;
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
        return actorNode.getName();
    }

    @Override
    public ActorPath getPath() {
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
    public Future<?> ask(Object o) {
        FutureImpl<Object> future = new FutureImpl<>();
        tell(o, future);
        return future;
    }
}
