package org.gethydrated.hydra.actors.timer;

import java.util.Set;

/**
 * Queues used by {@link Timer} to store scheduled tasks.
 *
 * For possible implementations, see George Varghese and Tony Lauck's paper at
 * <a href="http://www.cs.columbia.edu/~nahum/w6998/papers/sosp87-timing-wheels.pdf">
 *     http://www.cs.columbia.edu/~nahum/w6998/papers/sosp87-timing-wheels.pdf</a>
 *
 */
public interface TimerQueue {


    /**
     * Returns a queue dependent runnable, that is responsible
     * for tick generation.
     *
     * The executing thread is generated and handled by the calling
     * {@link Timer} implementation.
     *
     * !!! The queue is responsible for stopping its worker on shutdown !!!
     *
     * @return a queue dependent worker.
     */
    Runnable getQueueWorker();

    /**
     * Enqueue a new task in the {@link TimerQueue}. If the queue is already
     * stopped this must throw an {@link IllegalStateException}.
     * @param task new task
     * @return a {@link Timeout} object as handle to the scheduled task
     */
    Timeout enqueue(TimerTask task);

    /**
     * Stops current queue workers and returns all unhandled {@link Timeout} objects.
     * @return {@link Timeout} objects that were not yet handled by the queue worker.
     */
    Set<Timeout> stop();

}
