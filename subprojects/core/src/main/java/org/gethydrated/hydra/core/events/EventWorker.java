package org.gethydrated.hydra.core.events;

import org.gethydrated.hydra.core.messages.MessageQueue;

public class EventWorker implements Runnable {

    private MessageQueue mq;
    
    private boolean running = true;
    
    public EventWorker(MessageQueue mq) {
        this.mq = mq;
    }
    
    @Override
    public void run() {
        while (running) {
            try {
                Event e = mq.getEvent();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
                //TODO: Event handling
        }
    }

}
