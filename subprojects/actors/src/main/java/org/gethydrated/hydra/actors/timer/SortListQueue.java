package org.gethydrated.hydra.actors.timer;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Timer queue backed by a sorted list. The {@link TimerTask}s are sorted by due
 * date. If tasks have the same timestamp, the new tasks timestamp will be
 * incremented until there are no collisions.
 * 
 * The accuracy of Timers bases on this queue can be set by specifying the
 * wanted time slice size at creation. The default is 100ms.
 * 
 * For more information, see George Varghese and Tony Lauck's paper at <a href=
 * "http://www.cs.columbia.edu/~nahum/w6998/papers/sosp87-timing-wheels.pdf">
 * http://www.cs.columbia.edu/~nahum/w6998/papers/sosp87-timing-wheels.pdf</a>
 */
public class SortListQueue implements TimerQueue, Runnable {

    private final long timeSlice;

    private final AtomicBoolean stopped = new AtomicBoolean(false);

    private final Logger logger = LoggerFactory.getLogger(SortListQueue.class);

    private final boolean isWindows = System.getProperty("os.name")
            .toLowerCase().contains("win");

    private Thread timerThread;

    private final SortedSet<SortListTimeout> tasks = new ConcurrentSkipListSet<>(
            new Comparator<Timeout>() {
                @Override
                public int compare(final Timeout o1, final Timeout o2) {
                    return (int) (o1.getTask().getTimeStamp() - o2.getTask()
                            .getTimeStamp());
                }
            });

    /**
     * Creates a new SortListQueue with default time slice size of 100ms.
     */
    public SortListQueue() {
        this(100);
    }

    /**
     * Creates a new SortListQueue with the given time slice size.
     * 
     * @param timeSlice
     *            the time slice size.
     */
    public SortListQueue(final long timeSlice) {
        this.timeSlice = timeSlice;
    }

    @Override
    public Runnable getQueueWorker() {
        return this;
    }

    /**
     * Enqueue a new task in the {@link TimerQueue}. If the queue is already
     * stopped this must throw an {@link IllegalStateException}.
     * 
     * !!! Never enqueue the same {@link TimerTask} twice. This will result in
     * an endless loop !!!
     * 
     * @param task
     *            new task
     * @return a {@link Timeout} object as handle to the scheduled task
     */
    @Override
    public Timeout enqueue(final TimerTask task) {
        final SortListTimeout timeout = new SortListTimeout(
                Objects.requireNonNull(task));
        while (!tasks.add(timeout)) {
            timeout.getTask().incTimeStamp();
        }
        return timeout;
    }

    @Override
    public Set<Timeout> stop() {
        stopped.set(true);
        if (timerThread != null) {
            try {
                timerThread.join();
            } catch (final InterruptedException e) {
                logger.warn("Interrupted exception on timer queue.", e);
            }
        }
        return new HashSet<Timeout>(tasks);
    }

    @Override
    public void run() {
        timerThread = Thread.currentThread();
        while (!stopped.get()) {
            final long startTime = System.currentTimeMillis();
            expireTimeouts(startTime);
            final long currentTime = System.currentTimeMillis();
            long sleepTime = timeSlice - (startTime - currentTime);
            if (sleepTime > 0) {
                if (isWindows) {
                    sleepTime = (sleepTime / 10) * 10;
                    try {
                        Thread.sleep(sleepTime);
                    } catch (final InterruptedException e) {
                        logger.warn("Interrupted exception on timer queue.", e);
                    }
                }
            }
        }
    }

    private void expireTimeouts(final long startTime) {
        final Iterator<SortListTimeout> iterator = tasks.iterator();

        final Set<TimerTask> repeatables = new HashSet<>();

        while (iterator.hasNext()) {
            final SortListTimeout timeout = iterator.next();
            if (startTime > timeout.getTask().getTimeStamp()) {
                iterator.remove();
                if (timeout.isCancelled()) {
                    continue;
                }
                try {
                    timeout.getTask().execute();
                } catch (final Throwable t) {
                    logger.error("Error while executing timer task:", t);
                }
                if (timeout.getTask().isRepeatable()) {
                    timeout.getTask().incRepeat();
                    repeatables.add(timeout.getTask());
                }

            } else {
                break;
            }
        }
        for (final TimerTask t : repeatables) {
            enqueue(t);
        }
    }

    private static final class SortListTimeout implements Timeout {

        private final TimerTask task;

        private SortListTimeout(final TimerTask task) {
            this.task = task;
        }

        @Override
        public Timer getTimer() {
            return task.getTimer();
        }

        @Override
        public TimerTask getTask() {
            return task;
        }

        @Override
        public boolean isExpired() {
            return task.isExpired();
        }

        @Override
        public boolean cancel() {
            return task.cancel();
        }

        @Override
        public boolean isCancelled() {
            return task.isCancelled();
        }
    }

}
