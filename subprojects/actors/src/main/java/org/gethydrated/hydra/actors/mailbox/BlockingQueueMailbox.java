package org.gethydrated.hydra.actors.mailbox;

import org.gethydrated.hydra.actors.dispatch.Dispatcher;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * 
 * @author Christian Kulpa
 *
 */
public class BlockingQueueMailbox implements Mailbox {

    /**
     * Message fifo queue.
     */
    private final BlockingQueue<Message> messages = new LinkedBlockingQueue<>();

    private final BlockingQueue<Message> systemMessages = new LinkedBlockingQueue<>();

    private final Dispatcher dispatcher;

    private volatile boolean closed = false;

    private volatile boolean scheduled = false;

    private volatile boolean suspended = true;

    public BlockingQueueMailbox(final Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public final Message poll() {
        return messages.poll();
    }

    @Override
    public Message pollSystem() {
        return systemMessages.poll();
    }

    @Override
    public final void offer(Message m) {
        messages.offer(m);
        if(!scheduled) {
            dispatcher.registerForExecution(this);
        }
    }

    @Override
    public void offerSystem(Message m) {
        systemMessages.offer(m);
        if(!scheduled) {
            dispatcher.registerForExecution(this);
        }
    }

    public final boolean hasMessages() {
        return !messages.isEmpty() && !suspended;
    }

    @Override
    public boolean hasSystemMessages() {
        return !systemMessages.isEmpty();
    }

    @Override
    public boolean isScheduled() {
        return scheduled;
    }

    @Override
    public void setScheduled(boolean state) {
        scheduled = state;
    }

    @Override
    public void setSuspended(boolean state) {
        suspended = state;
    }

    @Override
    public boolean isSuspended() {
        return suspended;
    }

    @Override
    public void close() {
        closed = true;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

}
