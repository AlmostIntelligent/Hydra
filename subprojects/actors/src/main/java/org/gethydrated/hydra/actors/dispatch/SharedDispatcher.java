package org.gethydrated.hydra.actors.dispatch;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.gethydrated.hydra.actors.mailbox.BlockingQueueMailbox;
import org.gethydrated.hydra.actors.mailbox.Mailbox;
import org.gethydrated.hydra.actors.node.ActorNode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

/**
 *
 */
public class SharedDispatcher implements Dispatcher {

    ExecutorService executor = new ForkJoinPool();

    final BiMap<ActorNode, Mailbox> mailboxes = HashBiMap.create();

    @Override
    public Mailbox createMailbox(ActorNode actorNode) {
        synchronized(mailboxes) {
            if(mailboxes.containsValue(actorNode)) {
                return mailboxes.get(actorNode);
            } else {
                Mailbox mb = new BlockingQueueMailbox(this);
                mailboxes.put(actorNode, mb);
                return mb;
            }
        }
    }

    @Override
    public boolean closeMailbox(ActorNode actorNode) {
        Mailbox mb;
        synchronized (mailboxes) {
            mb = mailboxes.remove(actorNode);
        }
        if(mb != null) {
            mb.close();
            return true;
        }
        return false;
    }

    @Override
    public void registerForExecution(Mailbox mb) {
        if(mb.hasMessages() || mb.hasSystemMessages()) {
            ActorNode an;
            synchronized (mailboxes) {
                an = mailboxes.inverse().get(mb);
            }
            executor.submit(new MessageRunner(this, mb, an));
        }
    }

    @Override
    public void shutdown() {
        executor.shutdown();
    }
}
