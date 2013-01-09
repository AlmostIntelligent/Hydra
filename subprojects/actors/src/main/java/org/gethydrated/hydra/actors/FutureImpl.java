package org.gethydrated.hydra.actors;

import org.gethydrated.hydra.actors.mailbox.Message;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class FutureImpl<V> implements java.util.concurrent.Future<V>, ActorRef {

    private V value = null;
    private Throwable error = null;
    private Boolean done = false;
    private final Object lock = new Object();

    /**
     * We don't support removing messages from actor mailboxes (yet).
     * Cancel request always fails.
     * @param mayInterruptIfRunning don't care.
     * @return always false.
     */
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    /**
     * Cancellation not supported.
     * @return always false.
     */
    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        synchronized (lock) {
            return done;
        }
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        synchronized (lock) {
            while(!done) {
                lock.wait();
            }
            if(error!=null) {
                throw new ExecutionException(error);
            }
            return value;
        }
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        synchronized (lock) {
            if(!done) {
                unit.timedWait(lock, timeout);
            }
            if(!done){
                throw new TimeoutException();
            }
            if(error!=null) {
                throw new ExecutionException(error);
            }
            return value;
        }
    }

    @Override
    public String getName() {
        return "future";
    }

    @Override
    public ActorURI getAddress() {
        return null;  //TODO;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void tell(Object o, ActorRef sender) {
        synchronized (lock) {
            try {
                if(o instanceof Throwable) {
                    error = (Throwable)o;
                } else {
                    //noinspection unchecked
                    value = (V)o;
                }
            } catch (ClassCastException e) {
                error = e;
            }
            done = true;
            lock.notifyAll();
        }
    }

    @Override
    public void forward(Message m) {
        //TODO:
    }

    @Override
    public java.util.concurrent.Future<?> ask(Object o) {
        throw new RuntimeException("Cannot create future on future reference. Deadlock possible."); //TODO: actorexception
    }
}
