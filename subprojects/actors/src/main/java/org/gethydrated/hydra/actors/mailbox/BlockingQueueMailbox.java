package org.gethydrated.hydra.actors.mailbox;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.dispatch.Dispatcher;
import org.gethydrated.hydra.actors.node.ActorNode;

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

    private final ActorNode actorNode;

    private volatile boolean closed = false;

    private volatile boolean scheduled = false;

    private volatile boolean suspended = false;

    public BlockingQueueMailbox(final Dispatcher dispatcher, final  ActorNode actorNode) {
        this.dispatcher = dispatcher;
        this.actorNode = actorNode;
    }

    @Override
    public void enqueue(ActorRef self, Message m) {
        messages.offer(m);
    }

    @Override
    public void enqueueSystem(ActorRef self, Message m) {
        systemMessages.offer(m);
    }

    public final boolean hasMessages() {
        return !messages.isEmpty();
    }

    @Override
    public boolean hasSystemMessages() {
        return !systemMessages.isEmpty();
    }

    @Override
    public void setIdle() {
        scheduled = false;
    }

    @Override
    public boolean setScheduled() {
        if(closed || scheduled) {
            return false;
        }
        scheduled = true;
        return true;
    }

    @Override
    public void suspend() {
        suspended = true;
    }

    @Override
    public void resume() {
        suspended = false;
        dispatcher.executeMailbox(this, false, false);
    }

    @Override
    public boolean isSchedulable(boolean hasMessages, boolean hasSystemMessages) {
        if (closed) {
            return false;
        }
        if (!suspended) {
            return hasMessages || hasSystemMessages || hasMessages() || hasSystemMessages();
        }
        return hasSystemMessages || hasSystemMessages();
    }

    @Override
    public void setClosed() {
        closed = true;
    }

    @Override
    public void run() {
        try {
            if (!closed) {
                processSystemMessages();
                processMessages();
            }
        } finally {
            setIdle();
            dispatcher.executeMailbox(this, false, false);
        }
    }

    private void processSystemMessages() {
        InterruptedException interrupted = null;
        while (!systemMessages.isEmpty() && !closed) {
            Message m = systemMessages.remove();
            actorNode.handleSystemMessage(m);
            if (Thread.interrupted()) {
                interrupted = new InterruptedException("Interrupted while processing system messages.");
            }
        }
        if (interrupted != null) {
            Thread.interrupted();
            throw new RuntimeException(interrupted);
        }
    }

    private void processMessages() {
        while (!messages.isEmpty() && !closed) {
            Message m = messages.remove();
            actorNode.handleMessage(m);
            if (Thread.interrupted()) {
                throw new RuntimeException(new InterruptedException("Interrupted while processing messages."));
            }
        }
    }
}
