package org.gethydrated.hydra.actors.scheduling;

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
     * Stops current queue workers and returns all unhandled {@link Timed} objects.
     * @return {@link Timed} objects that were not yet handled by the queue worker.
     */
    Set<Timed> stop();

}
