package org.gethydrated.hydra.test.actors.timer;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.gethydrated.hydra.actors.timer.DefaultTimer;
import org.gethydrated.hydra.actors.timer.DefaultTimerTask;
import org.gethydrated.hydra.actors.timer.SortListQueue;
import org.gethydrated.hydra.actors.timer.Timeout;
import org.gethydrated.hydra.actors.timer.Timer;
import org.gethydrated.hydra.actors.timer.TimerTask;
import org.junit.Test;

/**
 * SortListQueue and Timer tests.
 */
public class SortListQueueTest {

    @Test
    public void testStartStop() throws InterruptedException {
        final SortListQueue slq = new SortListQueue();
        final Runnable r = slq.getQueueWorker();

        assertNotNull(r);

        final Thread t = new Thread(r);
        t.start();
        Thread.sleep(1000);
        final Set<Timeout> res = slq.stop();
        t.join();

        assertNotNull(res);
    }

    @Test
    public void testAddTasks() {
        final SortListQueue slq = new SortListQueue();
        final Timer timer = new DefaultTimer(slq);

        slq.enqueue(newTimerTask(timer));
        slq.enqueue(newTimerTask(timer));
        slq.enqueue(newTimerTask(timer));

        final Set<Timeout> res = slq.stop();

        assertTrue(res.size() == 3);
    }

    @Test
    public void testSortListQueueTimer() throws InterruptedException {
        final Timer timer = new DefaultTimer();
        final AtomicBoolean flag = new AtomicBoolean(false);
        final Timeout timeout = timer.schedule(1000, TimeUnit.MILLISECONDS,
                new Runnable() {
                    @Override
                    public void run() {
                        flag.set(true);
                    }
                });

        assertNotNull(timeout);

        Thread.sleep(900);
        assertFalse(flag.get());
        Thread.sleep(200);
        assertTrue(flag.get());

        final Set<Timeout> res = timer.stop();

        assertTrue(res.isEmpty());
    }

    @Test
    public void testTaskCancellation() throws InterruptedException {
        final Timer timer = new DefaultTimer();
        final AtomicBoolean flag = new AtomicBoolean(false);
        final Timeout timeout = timer.schedule(1000, TimeUnit.MILLISECONDS,
                new Runnable() {
                    @Override
                    public void run() {
                        flag.set(true);
                    }
                });

        assertNotNull(timeout);
        timeout.cancel();

        Thread.sleep(900);
        assertFalse(flag.get());
        Thread.sleep(200);
        assertFalse(flag.get());

        final Set<Timeout> res = timer.stop();

        assertTrue(res.isEmpty());
    }

    @Test
    public void testRepeatableTask() throws InterruptedException {
        final Timer timer = new DefaultTimer();
        final AtomicInteger result = new AtomicInteger(0);
        final Timeout timeout = timer.schedule(1000, TimeUnit.MILLISECONDS,
                200, TimeUnit.MILLISECONDS, new Runnable() {
                    @Override
                    public void run() {
                        result.incrementAndGet();
                    }
                });

        assertNotNull(timeout);

        Thread.sleep(2050);
        assertTrue(result.get() == 6);
        timeout.cancel();
        Thread.sleep(200);

        final Set<Timeout> res = timer.stop();

        assertTrue(res.isEmpty());
    }

    private TimerTask newTimerTask(final Timer timer) {
        return new DefaultTimerTask(timer, new Runnable() {
            @Override
            public void run() {
            }
        }, Executors.newSingleThreadExecutor(), 100);
    }

}
