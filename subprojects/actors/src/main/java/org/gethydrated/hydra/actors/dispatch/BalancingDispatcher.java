package org.gethydrated.hydra.actors.dispatch;

import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.mailbox.Mailbox;
import org.gethydrated.hydra.actors.node.ActorNode;

import java.util.concurrent.ExecutorService;

/**
 *
 */
public class BalancingDispatcher implements Dispatcher {
    @Override
    public Mailbox createMailbox(ActorNode actorNode) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean closeMailbox(ActorNode actorNode) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void registerForExecution(Mailbox mb) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void shutdown() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ExecutorService getExecutor() {
        return null;
    }

    @Override
    public Mailbox lookupMailbox(ActorPath path) {
        return null;
    }
}
