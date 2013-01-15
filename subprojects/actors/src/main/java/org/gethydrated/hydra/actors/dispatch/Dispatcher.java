package org.gethydrated.hydra.actors.dispatch;

import org.gethydrated.hydra.actors.mailbox.Mailbox;
import org.gethydrated.hydra.actors.mailbox.MailboxFactory;
import org.gethydrated.hydra.actors.mailbox.MailboxLookup;

import java.util.concurrent.ExecutorService;


public interface Dispatcher extends MailboxFactory, MailboxLookup {

    void registerForExecution(Mailbox mb);

    void shutdown();

    ExecutorService getExecutor();
}
