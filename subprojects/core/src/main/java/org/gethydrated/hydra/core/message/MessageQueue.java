package org.gethydrated.hydra.core.message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.gethydrated.hydra.api.message.Message;

/**
 * Message Queue.
 * 
 * @author Christian Kulpa
 *
 */
public class MessageQueue {
    
    /**
     * Queue.
     */
    private BlockingQueue<Message> queue = new LinkedBlockingQueue<>();
    
    /**
     * Adds a Message to the queue.
     * @param e Message.
     */
    public final void addMessage(final Message e) {
        queue.offer(e);
    }
    
    /**
     * Takes one message from the queue.
     * @return Message.
     * @throws InterruptedException 
     */
    public final Message getMessage() throws InterruptedException {
        return queue.take();
    }
}
