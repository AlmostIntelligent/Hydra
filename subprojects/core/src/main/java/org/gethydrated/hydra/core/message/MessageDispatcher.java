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

/**
 * Message Dispatcher.
 * 
 * @author Christian Kulpa
 *
 */
public class MessageDispatcher {
    
    /**
     * Logger instance.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MessageDispatcher.class);
  
    /**
     * Number of worker threads.
     */
    private final Integer constNumberOfWorkers = 10;
    
    /**
     * Threadpool for workers.
     */
    private final ExecutorService threadpool = Executors.newFixedThreadPool(constNumberOfWorkers);
    
    /**
     * Message queue.
     */
    private MessageQueue messageQueue;
    
    /**
     * Workers.
     */
    private MessageWorker[] workers = new MessageWorker[constNumberOfWorkers];
    
    /**
     * List of Handlers.
     */
    private Map<USID, List<WeakReference<MessageHandler>>> handlers = new ConcurrentHashMap<>();
    
    /**
     * Constructor. 
     * @param mq Queue.
     */
    public MessageDispatcher(final MessageQueue mq) {
        messageQueue = mq;
    }
    
    /**
     * Starting all workers.
     */
    public final void start() {
        LOG.debug("Starting message dispatcher.");
        for (int i = 0; i < constNumberOfWorkers; i++) {
            workers[i] = new MessageWorker();
            threadpool.execute(workers[i]);
        }
    }
    
    /**
     * Stops all workers.
     */
    public final void stop() {
        LOG.debug("Stopping message dispatcher.");
        threadpool.shutdownNow();
        try {
            while (!threadpool.awaitTermination(1, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            LOG.error("{}", e);
        }
    }

    /**
     * Adds a handler.
     * @param id Unique Service ID.
     * @param mh Message handler.
     */
    public final void addHandler(final USID id, final MessageHandler mh) {
        List<WeakReference<MessageHandler>> lh = handlers.get(id);
        if (lh == null) {
            lh = new LinkedList<>();
            handlers.put(id, lh);
        }
        synchronized (this) {
            lh.add(new WeakReference<MessageHandler>(mh));
        }
    }
    
    /**
     * Removes a handler.
     * @param id Unique Service ID.
     * @param mh Message handler.
     */
    public final void removeHandler(final USID id, final MessageHandler mh) {
        removeHandler(id, new WeakReference<MessageHandler>(mh));
    }
    
    /**
     * Removes a handler by a weak reference.
     * @param id Unique Service ID.
     * @param wf Weak reference.
     */
    private void removeHandler(final USID id, final WeakReference<MessageHandler> wf) {
        List<WeakReference<MessageHandler>> lh = handlers.get(id);
        if (lh != null) {
            synchronized (this) {
                lh.remove(wf);
            }
        }
    }
    
    /**
     * Message dispatcher worker.
     * @author Christian Kulpa
     *
     */
    private class MessageWorker implements Runnable {

        @Override
        public void run() {
            try {
                Message m = messageQueue.getMessage();
                List<WeakReference<MessageHandler>> h = handlers.get(m.getDestination());
                if (h != null && !h.isEmpty()) {
                    for (WeakReference<MessageHandler> wf : h) {
                        MessageHandler handler = wf.get();
                        if (handler != null) {
                            handler.processMessage(m);
                        } else {
                            removeHandler(m.getDestination(), wf);
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
