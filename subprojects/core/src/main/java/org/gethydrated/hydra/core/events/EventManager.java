package org.gethydrated.hydra.core.events;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.gethydrated.hydra.core.messages.MessageQueue;

public class EventManager {
    
    private MessageQueue queue;
    
    private final ExecutorService threadpool;
    
    
    public EventManager(MessageQueue mq) {
        queue = mq;
        threadpool = Executors.newFixedThreadPool(10);
    }
    
    public void start() {
        for(int i = 0; i < 10; i++) {
            
        }
    }
    
    public void stop() {
        threadpool.shutdown();
        try {
            threadpool.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
