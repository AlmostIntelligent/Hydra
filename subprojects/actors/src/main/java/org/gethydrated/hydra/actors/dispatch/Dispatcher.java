package org.gethydrated.hydra.actors.dispatch;

import org.gethydrated.hydra.actors.mailbox.Mailbox;
import org.gethydrated.hydra.actors.mailbox.Message;
import org.gethydrated.hydra.actors.node.ActorNode;

import java.util.concurrent.ExecutorService;


public interface Dispatcher {

    Mailbox createMailbox(ActorNode node);

    void attach(ActorNode node);

    void detach(ActorNode node);

    void dispatch(ActorNode node, Message message);

    void dispatchSystem(ActorNode node, Message message);

    boolean executeMailbox(Mailbox mailbox, boolean hasMessages, boolean hasSystemMessages);

    ExecutorService getExecutor();

    void shutdown();

    void join();
}
