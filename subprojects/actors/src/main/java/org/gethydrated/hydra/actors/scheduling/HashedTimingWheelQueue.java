package org.gethydrated.hydra.actors.scheduling;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Timer queue implemented as hash wheel. Each {@link TimerTask} is scheduled
 * in the appropriate wheel slot.
 * <p/>
 * On every tick, the {@link HashedTimingWheelQueue} will check if there are any
 * {@link TimerTask}s ready to be executed. The {@link TimerTask}s are stored
 * in an ordered set for each wheel slot.
 * <p/>
 * This {@link TimerQueue} implements Runnable itself, so no extra worker is needed.
 * <p/>
 * For more information, see George Varghese and Tony Lauck's paper at
 * <a href="http://www.cs.columbia.edu/~nahum/w6998/papers/sosp87-timing-wheels.pdf">
 *     http://www.cs.columbia.edu/~nahum/w6998/papers/sosp87-timing-wheels.pdf</a>
 */
class HashedTimingWheelQueue implements TimerQueue, Runnable {

    private final AtomicBoolean stopped = new AtomicBoolean(false);

    private final Set<Timed>[] wheel;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();



    public HashedTimingWheelQueue() {
        this(512, 100, TimeUnit.MILLISECONDS);
    }

    public HashedTimingWheelQueue(long timeSlice, TimeUnit timeUnit) {
        this(512, timeSlice, timeUnit);
    }

    public HashedTimingWheelQueue(int wheelSize) {
        this(wheelSize, 100, TimeUnit.MILLISECONDS);
    }

    public HashedTimingWheelQueue(int wheelSize, long timeSlice, TimeUnit timeUnit) {
        wheel = createWheel(wheelSize);
    }

    @SuppressWarnings("unchecked")
    private Set<Timed>[] createWheel(int wheelSize) {
        Set<Timed>[] setarray = new Set[wheelSize];
        for(int i = 0; i < setarray.length; i++) {
            setarray[i] = new HashSet<>();
        }
        return setarray;
    }

    @Override
    public Runnable getQueueWorker() {
        return this;
    }

    @Override
    public Set<Timed> stop() {
        stopped.set(true);
        return null;
    }

    @Override
    public void run() {
        while (!stopped.get()) {

        }
    }
}
