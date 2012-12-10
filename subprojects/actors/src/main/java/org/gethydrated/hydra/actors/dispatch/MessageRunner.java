package org.gethydrated.hydra.actors.dispatch;

import org.gethydrated.hydra.actors.mailbox.Mailbox;
import org.gethydrated.hydra.actors.mailbox.Message;
import org.gethydrated.hydra.actors.node.ActorNode;

/**
 *
 */
public class MessageRunner implements Runnable {

    private final Mailbox mailbox;

    private final Dispatcher dispatcher;

    private final ActorNode actorNode;

    public MessageRunner(Dispatcher dispatcher, Mailbox mb, ActorNode actorNode) {
        this.mailbox = mb;
        this.dispatcher = dispatcher;
        this.actorNode = actorNode;
    }

    @Override
    public void run() {
        processSystemMessages();
        processMessages();
        if(mailbox.hasSystemMessages() || mailbox.hasMessages()) {
            dispatcher.registerForExecution(mailbox);
        }
    }

    private void processSystemMessages() {
        while(mailbox.hasSystemMessages()) {
            Message m = mailbox.pollSystem();
            if(m != null) {
                actorNode.processSystem(m);
            }
        }
    }

    private void processMessages() {
        while(mailbox.hasMessages()) {
            Message m = mailbox.poll();
            if(m != null) {
                actorNode.process(m);
            }
        }
    }
}
