package org.gethydrated.hydra.actors;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Synchronization variable for ask requests. 
 * 
 * @author Chris
 * @since 0.2.0
 * @param <V> Result type.
 */
public class SyncVar<V> implements java.util.concurrent.Future<V> {

    private V value = null;
    private Throwable error = null;
    private Boolean done = false;
    private final Object lock = new Object();

    /**
     * We don't support removing messages from actor mailboxes (yet). Cancel
     * request always fails.
     * 
     * @param mayInterruptIfRunning
     *            don't care.
     * @return always false.
     */
    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return false;
    }

    /**
     * Cancellation not supported.
     * 
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
            while (!done) {
                lock.wait();
            }
            if (error != null) {
                throw new ExecutionException(error);
            }
            return value;
        }
    }

    @Override
    public V get(final long timeout, final TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        synchronized (lock) {
            if (!done) {
                unit.timedWait(lock, timeout);
            }
            if (!done) {
                throw new TimeoutException("Timeout while waiting for result.");
            }
            if (error != null) {
                throw new ExecutionException(error);
            }
            return value;
        }
    }

    /**
     * Sets the result to success.
     * @param value result.
     */
    public void put(final V value) {
        synchronized (lock) {
            if (!done) {
                this.value = value;
                done = true;
                lock.notifyAll();
            }
        }
    }

    /**
     * Sets the result to failure.
     * @param t Cause of failure.
     */
    public void fail(final Throwable t) {
        synchronized (lock) {
            if (!done) {
                error = t;
                done = true;
                lock.notifyAll();
            }
        }
    }
}
