package org.gethydrated.hydra.actors.dispatch;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.mailbox.BlockingQueueMailbox;
import org.gethydrated.hydra.actors.mailbox.Mailbox;
import org.gethydrated.hydra.actors.node.ActorNode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 *
 */
public class SharedDispatcher implements Dispatcher {

    final ExecutorService executor;

    final BiMap<ActorNode, Mailbox> mailboxes = HashBiMap.create();

    public SharedDispatcher(Thread.UncaughtExceptionHandler handler) {
        executor = new ForkJoinPool(10, ForkJoinPool.defaultForkJoinWorkerThreadFactory, handler, false);
    }

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

    public Mailbox lookupMailbox(ActorPath path) {
        System.out.println(mailboxes.containsKey(path));
        for(ActorNode n : mailboxes.keySet()) {
            System.out.println(n.getName() + ": " + n.hashCode() + "-" + path.hashCode() + " -> " + path.equals(n));
        }
        System.out.println(mailboxes.get(path));
        return mailboxes.get(path);
    }

    @Override
    public void registerForExecution(Mailbox mb) {
        if(mb.hasMessages() || mb.hasSystemMessages()) {
            ActorNode an;
            synchronized (mailboxes) {
                an = mailboxes.inverse().get(mb);
            }
            executor.execute(new MessageRunner(this, mb, an));
        }
    }

    @Override
    public void shutdown() {
        executor.shutdown();
    }

    @Override
    public ExecutorService getExecutor() {
        return executor;  //TODO: Maybe wrap that into a delegate to shield the actual executor.
    }

    private final class MailboxTask extends ForkJoinTask<Object> {

        private MessageRunner runner;

        public MailboxTask(MessageRunner runner) {
            this.runner = runner;
        }

        @Override
        public Object getRawResult() {
            return null;
        }

        @Override
        protected void setRawResult(Object value) { }

        @Override
        protected boolean exec() {
            try {
                runner.run();
                return true;
            } catch (Throwable t) {
                Thread th = Thread.currentThread();
                th.getDefaultUncaughtExceptionHandler().uncaughtException(th, t);
                throw t;
            }
        }
    }
}
