package org.gethydrated.hydra.actors.timer;

import org.gethydrated.hydra.actors.ActorRef;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Default {@link Timer} implementation. Uses {@link SortListQueue} if no
 * other {@link TimerQueue} is provided to store scheduled tasks.
 *
 * The needed background task is started lazily on the first scheduled task.
 *
 */
public class DefaultTimer implements Timer{

    private Thread timerThread;

    private final TimerQueue timerQueue;

    private final ThreadFactory threadFactory;

    private AtomicBoolean shutdown = new AtomicBoolean(false);

    /**
     * Creates a new {@link Timer} with default thread factory and default
     * {@link SortListQueue}.
     */
    public DefaultTimer() {
        this(Executors.defaultThreadFactory());
    }

    /**
     * Chreates a new {@link Timer} with default {@link SortListQueue}.
     * @param tf used thread factory.
     */
    public DefaultTimer(ThreadFactory tf) {
        this(tf, new SortListQueue());
    }

    /**
     * Creates a new {@link Timer} with default thread factory and the given queue.
     * @param tq used timer queue.
     */
    public DefaultTimer(TimerQueue tq) {
        this(Executors.defaultThreadFactory(), tq);
    }

    /**
     * Creates a new {@link Timer} with the given thread factory and queue.
     * @param tf used thread factory
     * @param tq used {@link TimerQueue}
     */
    public DefaultTimer(ThreadFactory tf, TimerQueue tq) {
        timerQueue = Objects.requireNonNull(tq);
        threadFactory = Objects.requireNonNull(tf);
    }

    private synchronized void start() {
        if(shutdown.get()) {
            throw new IllegalStateException("Timer already stopped.");
        }
        if(timerThread == null || !timerThread.isAlive()) {
            timerThread = threadFactory.newThread(timerQueue.getQueueWorker());
            timerThread.start();
        }
    }

    @Override
    public synchronized Set<Timeout> stop() {
        if (Thread.currentThread() == timerThread) {
            throw new IllegalStateException("Cannot call Timer.stop() from internal worker thread.");
        }
        return timerQueue.stop();
    }

    @Override
    public Timeout schedule(long delay, TimeUnit timeUnit, Runnable task) {
        return schedule(delay, timeUnit, 0, timeUnit, task, null);
    }

    @Override
    public Timeout schedule(long delay, TimeUnit timeUnitDelay, long repeat, TimeUnit timeUnitRepeat, Runnable task) {
        return schedule(delay, timeUnitDelay, repeat, timeUnitRepeat, task, null);
    }

    @Override
    public Timeout schedule(long delay, TimeUnit timeUnit, Runnable task, Executor executor) {
        return schedule(delay, timeUnit, 0, timeUnit, task, executor);
    }

    @Override
    public Timeout schedule(long delay, TimeUnit timeUnitDelay, long repeat, TimeUnit timeUnitRepeat, Runnable task, Executor executor) {
        long timestamp = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(delay, timeUnitDelay);
        long repeatTime = TimeUnit.MILLISECONDS.convert(repeat, timeUnitRepeat);
        TimerTask t = new DefaultTimerTask(this,task, executor, timestamp, repeatTime);
        start();
        return timerQueue.enqueue(t);
    }

    @Override
    public Timeout schedule(long delay, TimeUnit timeUnit, final ActorRef ref, final Object message) {
        return schedule(delay, timeUnit, 0, timeUnit, new Runnable() {
            @Override
            public void run() {
                ref.tell(message, null);
            }
        }, null);
    }

    @Override
    public Timeout schedule(long delay, TimeUnit timeUnitDelay, long repeat, TimeUnit timeUnitRepeat, final ActorRef ref, final Object message) {
        return schedule(delay, timeUnitDelay, repeat, timeUnitRepeat, new Runnable() {
            @Override
            public void run() {
                ref.tell(message, null);
            }
        }, null);
    }

    @Override
    public Timeout schedule(long delay, TimeUnit timeUnit, final ActorRef ref, final Object message, Executor executor) {
        return schedule(delay, timeUnit, 0, timeUnit, new Runnable() {
            @Override
            public void run() {
                ref.tell(message, null);
            }
        }, executor);
    }

    @Override
    public Timeout schedule(long delay, TimeUnit timeUnitDelay, long repeat, TimeUnit timeUnitRepeat, final ActorRef ref, final Object message, Executor executor) {
        return schedule(delay, timeUnitDelay, repeat, timeUnitRepeat, new Runnable() {
            @Override
            public void run() {
                ref.tell(message, null);
            }
        }, executor);
    }

}
