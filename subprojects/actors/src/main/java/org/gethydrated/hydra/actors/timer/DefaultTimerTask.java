package org.gethydrated.hydra.actors.timer;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Default {@link TimerTask} implementation.
 */
public class DefaultTimerTask implements TimerTask {

    private final Timer timer;

    private final long repeat;

    private volatile long timestamp;

    private Executor executor;

    private Runnable action;

    private final AtomicBoolean cancelled = new AtomicBoolean(false);

    private final AtomicBoolean expired = new AtomicBoolean(false);

    /**
     * Creates a new non repeating TimerTask.
     * 
     * @param timer
     *            the {@link Timer} that created this task.
     * @param action
     *            the actual action that is executed on expiration.
     * @param executor
     *            the executor that will handle task execution. Can be null.
     * @param timestamp
     *            the scheduled time
     */
    public DefaultTimerTask(final Timer timer, final Runnable action,
            final Executor executor, final long timestamp) {
        this(timer, action, executor, timestamp, 0);
    }

    /**
     * Creates a new repeating TimerTask.
     * 
     * @param timer
     *            the {@link Timer} that created this task.
     * @param action
     *            the actual action that is executed on expiration.
     * @param executor
     *            the executor that will handle task execution. Can be null.
     * @param timestamp
     *            the scheduled time
     * @param repeat
     *            repetition delay
     */
    public DefaultTimerTask(final Timer timer, final Runnable action,
            final Executor executor, final long timestamp, final long repeat) {
        this.timer = Objects.requireNonNull(timer);
        this.action = Objects.requireNonNull(action);
        this.executor = executor;
        this.timestamp = timestamp;
        this.repeat = repeat;
    }

    @Override
    public Timer getTimer() {
        return timer;
    }

    @Override
    public long getTimeStamp() {
        return timestamp;
    }

    @Override
    public void incTimeStamp() {
        timestamp++;
    }

    @Override
    public void incRepeat() {
        if (repeat > 0) {
            timestamp += repeat;
        }
    }

    @Override
    public boolean isRepeatable() {
        return (repeat > 0);
    }

    @Override
    public void execute() throws Exception {
        if (!cancelled.get() && !expired.get()) {
            if (executor != null) {
                executor.execute(action);
            } else {
                action.run();
            }
            if (!isRepeatable()) {
                expired.set(true);
            }
        }
    }

    @Override
    public boolean isExpired() {
        return expired.get();
    }

    @Override
    public boolean cancel() {
        if (expired.get()) {
            return false;
        }
        cancelled.set(true);
        executor = null;
        action = null;
        return true;
    }

    @Override
    public boolean isCancelled() {
        return cancelled.get();
    }
}
