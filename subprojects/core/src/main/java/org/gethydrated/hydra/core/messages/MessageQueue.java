package org.gethydrated.hydra.core.messages;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.gethydrated.hydra.core.events.Event;

public class MessageQueue {
    
    private BlockingQueue<Event> queue = new LinkedBlockingQueue<>();
    
    public void addEvent(Event e) throws InterruptedException {
        queue.put(e);
    }
    
    public Event getEvent() throws InterruptedException {
        return queue.take();
    }
}
