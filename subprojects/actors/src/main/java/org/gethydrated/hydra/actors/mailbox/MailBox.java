package org.gethydrated.hydra.actors.mailbox;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * 
 * @author Christian Kulpa
 *
 */
public class MailBox {

    /**
     * Message fifo queue.
     */
    private BlockingQueue<Message> mbox = new LinkedBlockingQueue<>();
    
    /**
     * Offers a message to the mailbox.
     * @param m new Message.
     */
    public final void put(final Message m) {
        mbox.offer(m);
    }
    
    /**
     * Retrieves a message from the mailbox. 
     * @return first message of the mailbox.
     * @throws InterruptedException on interrupt.
     */
    public final Message get() throws InterruptedException {
        return mbox.take();
    }
}
