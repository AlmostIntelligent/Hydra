package org.gethydrated.hydra.actors.scheduling;

import org.gethydrated.hydra.actors.ActorRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Default {@link Timer} implementation. Uses {@link HashedTimingWheelQueue} if no
 * other {@link TimerQueue} is provided to store scheduled tasks.
 *
 * The needed background task is started lazily on the first scheduled task.
 *
 */
public class DefaultTimer implements Timer{

    private final Logger logger = LoggerFactory.getLogger(DefaultTimer.class);

    private Thread timerThread;

    private final TimerQueue timerQueue;

    private final ThreadFactory threadFactory;

    private AtomicBoolean shutdown = new AtomicBoolean(false);

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Creates a new {@link Timer} with default thread factory and default
     * {@link HashedTimingWheelQueue}.
     */
    public DefaultTimer() {
        this(Executors.defaultThreadFactory());
    }

    /**
     * Chreates a new {@link Timer} with default {@link HashedTimingWheelQueue}.
     * @param tf used thread factory.
     */
    public DefaultTimer(ThreadFactory tf) {
        this(tf, new HashedTimingWheelQueue());
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
    public synchronized Set<Timed> stop() {
        if (Thread.currentThread() == timerThread) {
            throw new IllegalStateException("Cannot call Timer.stop() from internal worker thread.");
        }
        return null;
    }

    @Override
    public Timed schedule(long delay, TimeUnit timeUnit, Runnable task) {
        return null;
    }

    @Override
    public Timed schedule(long delay, TimeUnit timeUnitDelay, long repeat, TimeUnit timeUnitRepeat, Runnable task) {
        return null;
    }

    @Override
    public Timed schedule(long delay, TimeUnit timeUnit, Runnable task, Executor executor) {
        return null;
    }

    @Override
    public Timed schedule(long delay, TimeUnit timeUnitDelay, long repeat, TimeUnit timeUnitRepeat, Runnable task, Executor executor) {
        return null;
    }

    @Override
    public Timed schedule(long delay, TimeUnit timeUnit, ActorRef ref, Object message) {
        return null;
    }

    @Override
    public Timed schedule(long delay, TimeUnit timeUnitDelay, long repeat, TimeUnit timeUnitRepeat, ActorRef ref, Object message) {
        return null;
    }

    @Override
    public Timed schedule(long delay, TimeUnit timeUnit, ActorRef ref, Object message, Executor executor) {
        return null;
    }

    @Override
    public Timed schedule(long delay, TimeUnit timeUnitDelay, long repeat, TimeUnit timeUnitRepeat, ActorRef ref, Object message, Executor executor) {
        return null;
    }

}
