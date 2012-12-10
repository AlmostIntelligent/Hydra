package org.gethydrated.hydra.actors.mailbox;

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
    private final BlockingQueue<Message> mbox = new LinkedBlockingQueue<>();
    
    /**
     * Retrieves a message from the mailbox. 
     * @return first message of the mailbox.
     * @throws InterruptedException on interrupt.
     */
    public final Message take() throws InterruptedException {
        return mbox.take();
    }

    @Override
    public final Message poll() {
        return mbox.poll();
    }

    @Override
    public final void offer(Message m) {
        mbox.offer(m);
    }
    
    public final boolean hasMessages() {
        return !mbox.isEmpty();
    }
    
}
