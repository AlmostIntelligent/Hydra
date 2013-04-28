package org.gethydrated.hydra.actors.dispatch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import org.gethydrated.hydra.actors.mailbox.BlockingQueueMailbox;
import org.gethydrated.hydra.actors.mailbox.Mailbox;
import org.gethydrated.hydra.actors.mailbox.Message;
import org.gethydrated.hydra.actors.node.ActorNode;
import org.slf4j.LoggerFactory;


/**
 *
 */
public class SharedDispatcher implements Dispatcher {

    private final ForkJoinPool executor;

    //private final BiMap<ActorNode, Mailbox> mailboxes = HashBiMap.create();

    /**
     * Constructor.
     * @param handler exception handler.
     */
    public SharedDispatcher(final Thread.UncaughtExceptionHandler handler) {
        executor = new ForkJoinPool(10,
                ForkJoinPool.defaultForkJoinWorkerThreadFactory, handler, false);
    }

    @Override
    public Mailbox createMailbox(final ActorNode actorNode) {
        return new BlockingQueueMailbox(this, actorNode);
    }

    @Override
    public void attach(final ActorNode node) {
        executeMailbox(node.getMailbox(), false, true);
    }

    @Override
    public void detach(final ActorNode node) {
        node.getMailbox().setClosed();
    }

    @Override
    public void dispatch(final ActorNode node, final Message message) {
        final Mailbox mb = node.getMailbox();
        mb.enqueue(node.getSelf(), message);
        executeMailbox(mb, true, false);
    }

    @Override
    public void dispatchSystem(final ActorNode node, final Message message) {
        final Mailbox mb = node.getMailbox();
        mb.enqueueSystem(node.getSelf(), message);
        executeMailbox(mb, false, true);
    }

    @Override
    public boolean executeMailbox(final Mailbox mailbox,
            final boolean hasMessages, final boolean hasSystemMessages) {
        if (mailbox.isSchedulable(hasMessages, hasSystemMessages)) {
            if (mailbox.setScheduled()) {
                try {
                    executor.execute(new MailboxTask(mailbox));
                    return true;
                } catch (final RejectedExecutionException e) {
                    mailbox.setIdle();
                    LoggerFactory.getLogger(SharedDispatcher.class).error(
                            "Executor rejected mailbox execution.", e);
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
        return executor;
    }

    @Override
    public void join() {
        try {
            while (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
            }
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fork-join mailbox task.
     * 
     * @author Christian Kulpa
     * @since 0.2.0
     */
    private final class MailboxTask extends ForkJoinTask<Object> {

        private static final long serialVersionUID = -6104082398296082833L;
        private final Mailbox mailbox;

        public MailboxTask(final Mailbox mailbox) {
            this.mailbox = mailbox;
        }

        @Override
        public Object getRawResult() {
            return null;
        }

        @Override
        protected void setRawResult(final Object value) {
        }

        @Override
        protected boolean exec() {
            try {
                mailbox.run();
                return true;
            } catch (final Throwable t) {
                final Thread th = Thread.currentThread();
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(
                        th, t);
                throw t;
            }
        }
    }
}
