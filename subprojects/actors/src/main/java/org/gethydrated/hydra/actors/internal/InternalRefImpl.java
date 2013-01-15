package org.gethydrated.hydra.actors.internal;

import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.SystemMessages.Start;
import org.gethydrated.hydra.actors.SystemMessages.Stop;
import org.gethydrated.hydra.actors.mailbox.Mailbox;
import org.gethydrated.hydra.actors.mailbox.Message;

public class InternalRefImpl extends AbstractActorRef implements InternalRef {

    public InternalRefImpl(ActorPath path) {
        super(path);
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
    public void watch(ActorRef target) {

    }

    @Override
    public void unwatch(ActorRef target) {

    }

    @Override
    public void tellSystem(Object o, ActorRef ref) {
        getMailbox().offerSystem(new Message(o, ref));
    }

    @Override
    protected Mailbox getMailbox() {
        return null; //TODO:
    }
}
