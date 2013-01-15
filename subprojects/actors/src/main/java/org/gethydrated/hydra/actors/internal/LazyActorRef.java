package org.gethydrated.hydra.actors.internal;

import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.dispatch.Dispatchers;
import org.gethydrated.hydra.actors.mailbox.Mailbox;

/**
 *
 */
public class LazyActorRef extends AbstractActorRef {

    Mailbox mb = null;

    Dispatchers dispatchers;

    public LazyActorRef(ActorPath path, Dispatchers dispatchers) {
        super(path);
        this.dispatchers = dispatchers;
    }

    @Override
    protected Mailbox getMailbox() {
        if(mb == null)
            mb = dispatchers.lookupMailbox(getPath());
        if(mb == null) {
            throw new RuntimeException("Actor not found: '"+getPath()+"'");
        }
        return mb;
    }
}
