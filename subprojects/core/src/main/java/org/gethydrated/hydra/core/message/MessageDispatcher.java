package org.gethydrated.hydra.core.message;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import org.gethydrated.hydra.api.message.Message;
import org.gethydrated.hydra.api.message.MessageHandler;
import org.gethydrated.hydra.api.service.USID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageDispatcher {
    
    private static final Logger LOG = LoggerFactory.getLogger(MessageDispatcher.class);
    
    private final ExecutorService threadpool = Executors.newFixedThreadPool(10);
    private MessageQueue messageQueue;
    private MessageWorker[] workers = new MessageWorker[10];
    
    
    private Map<USID, List<WeakReference<MessageHandler>>> handlers = new ConcurrentHashMap<>();
    
    public MessageDispatcher(MessageQueue mq) {
        messageQueue = mq;
    }
    
    public void start() {
        LOG.debug("Starting message dispatcher.");
        for(int i = 0; i < 10; i++) {
            workers[i] = new MessageWorker();
            threadpool.execute(workers[i]);
        }
    }
    
    public void stop() {
        LOG.debug("Stopping message dispatcher.");
        threadpool.shutdownNow();
        try {
            while(!threadpool.awaitTermination(1, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            LOG.error("{}",e);
        }
    }

    public void addHandler(USID id, MessageHandler mh) {
        List<WeakReference<MessageHandler>> lh = handlers.get(id);
        if(lh == null) {
            lh = new LinkedList<>();
            handlers.put(id, lh);
        }
        synchronized (this) {
            lh.add(new WeakReference<MessageHandler>(mh));
        }
    }
    
    public void removeHandler(USID id, MessageHandler mh) {
        removeHandler(id, new WeakReference<MessageHandler>(mh));
    }
    
    private void removeHandler(USID id, WeakReference<MessageHandler> wf) {
        List<WeakReference<MessageHandler>> lh = handlers.get(id);
        if(lh != null) {
            synchronized (this) {
                lh.remove(wf);
            }
        }
    }
    
    private class MessageWorker implements Runnable {

        @Override
        public void run() {
            try {
                Message m = messageQueue.getMessage();
                List<WeakReference<MessageHandler>> h = handlers.get(m.getTarget());
                if(h != null && !h.isEmpty()) {
                    for(WeakReference<MessageHandler> wf : h) {
                        MessageHandler handler = wf.get();
                        if(handler != null) {
                            handler.processMessage(m);
                        } else {
                            removeHandler(m.getTarget(),wf);
                        }
                    }
                } else {
                    LOG.trace("Dropped Message. (No handlers found)");
                }
            } catch (InterruptedException e) {
            }
            try {
                threadpool.execute(this);
            } catch (RejectedExecutionException e) {
                
            }

        }
        
    }


}
