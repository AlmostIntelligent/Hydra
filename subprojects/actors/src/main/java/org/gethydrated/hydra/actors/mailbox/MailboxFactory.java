package org.gethydrated.hydra.actors.mailbox;

import org.gethydrated.hydra.actors.node.ActorNode;

/**
 *
 */
public interface MailboxFactory {

    Mailbox createMailbox(ActorNode actorNode);

    boolean closeMailbox(ActorNode actorNode);
}
