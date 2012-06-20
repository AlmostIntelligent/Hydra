package org.gethydrated.hydra.core.message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.gethydrated.hydra.api.message.Message;

public class MessageQueue {
    
    private BlockingQueue<Message> queue = new LinkedBlockingQueue<>();
    
    public void addMessage(Message e) {
        queue.offer(e);
    }
    
    public Message getMessage() throws InterruptedException {
        return queue.take();
    }
}
