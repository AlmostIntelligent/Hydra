package org.gethydrated.hydra.actors.dispatch;

import org.gethydrated.hydra.actors.mailbox.Mailbox;
import org.gethydrated.hydra.actors.node.ActorNode;

/**
 *
 */
public class SharedDispatcher implements Dispatcher {

    @Override
    public Mailbox createMailbox(ActorNode actorNode) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean closeMailbox(ActorNode actorNode) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
