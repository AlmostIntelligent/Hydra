package org.gethydrated.hydra.actors.mailbox;

import org.gethydrated.hydra.actors.ActorPath;

/**
 *
 */
public interface MailboxLookup {
    Mailbox lookupMailbox(ActorPath path);
}
