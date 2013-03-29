package org.gethydrated.hydra.actors.dispatch;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.gethydrated.hydra.actors.mailbox.BlockingQueueMailbox;
import org.gethydrated.hydra.actors.mailbox.Mailbox;
import org.gethydrated.hydra.actors.mailbox.Message;
import org.gethydrated.hydra.actors.node.ActorNode;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 *
 */
public class SharedDispatcher implements Dispatcher {

    final ForkJoinPool executor;

    final BiMap<ActorNode, Mailbox> mailboxes = HashBiMap.create();

    public SharedDispatcher(Thread.UncaughtExceptionHandler handler) {
        executor = new ForkJoinPool(10, ForkJoinPool.defaultForkJoinWorkerThreadFactory, handler, false);
    }

    @Override
    public Mailbox createMailbox(ActorNode actorNode) {
        return new BlockingQueueMailbox(this, actorNode);
    }

    @Override
    public void attach(ActorNode node) {
        executeMailbox(node.getMailbox(), false, true);
    }

    @Override
    public void detach(ActorNode node) {
        node.getMailbox().setClosed();
    }

    @Override
    public void dispatch(ActorNode node, Message message) {
        Mailbox mb = node.getMailbox();
        mb.enqueue(node.getSelf(), message);
        executeMailbox(mb, true, false);
    }

    @Override
    public void dispatchSystem(ActorNode node, Message message) {
        Mailbox mb = node.getMailbox();
        mb.enqueueSystem(node.getSelf(), message);
        executeMailbox(mb, false, true);
    }

    public boolean executeMailbox(Mailbox mailbox, boolean hasMessages, boolean hasSystemMessages) {
        if (mailbox.isSchedulable(hasMessages, hasSystemMessages)) {
            if (mailbox.setScheduled()) {
                try {
                    executor.execute(new MailboxTask(mailbox));
                    return true;
                } catch (RejectedExecutionException e) {
                    mailbox.setIdle();
                    LoggerFactory.getLogger(SharedDispatcher.class).error("Executor rejected mailbox execution.", e);
                }
            }
        }
        return false;
    }

    @Override
    public void shutdown() {
        executor.shutdown();
    }

    @Override
    public ExecutorService getExecutor() {
        return executor;  //TODO: Maybe wrap that into a delegate to shield the actual executor.
    }

    @Override
    public void join() {
        try {
            while(!executor.awaitTermination(1, TimeUnit.SECONDS)) {}
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private final class MailboxTask extends ForkJoinTask<Object> {

        private Mailbox mailbox;

        public MailboxTask(Mailbox mailbox) {
            this.mailbox = mailbox;
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
                mailbox.run();
                return true;
            } catch (Throwable t) {
                Thread th = Thread.currentThread();
                th.getDefaultUncaughtExceptionHandler().uncaughtException(th, t);
                throw t;
            }
        }
    }
}
