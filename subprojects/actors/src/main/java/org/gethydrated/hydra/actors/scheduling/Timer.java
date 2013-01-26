package org.gethydrated.hydra.actors.scheduling;

import org.gethydrated.hydra.actors.ActorRef;

import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 *  A facility for threads to schedule tasks for future execution in a background thread.
 *  Tasks may be scheduled for one-time execution, or for repeated execution at regular intervals.
 *
 *  @since 0.2.0
 *  @author Christian Kulpa
 */
public interface Timer {

    /**
     * Schedules a task to run after an initial delay. Will be executed by the default executor.
     * @param delay initial delay
     * @param timeUnit delay granularity
     * @param task scheduled task
     * @return a {@link Timed} object as handle to the scheduled task
     * @throws IllegalStateException if the task could not be scheduled.
     */
    Timed schedule(long delay, TimeUnit timeUnit, Runnable task);

    /**
     * Schedules a task to run after an initial delay and then repeatedly at a fixed rate until
     * cancellation. Will be executed by the default executor.
     * @param delay initial delay
     * @param timeUnitDelay delay granularity
     * @param repeat repetition delay
     * @param timeUnitRepeat repetition delay granularity
     * @param task scheduled task
     * @return a {@link Timed} object as handle to the scheduled task
     * @throws IllegalStateException if the task could not be scheduled.
     */
    Timed schedule(long delay, TimeUnit timeUnitDelay, long repeat, TimeUnit timeUnitRepeat, Runnable task);

    /**
     * Schedules a task to run after an initial delay. The execution is handled by the given executor.
     * @param delay initial delay
     * @param timeUnit delay granularity
     * @param task scheduled task
     * @param executor executor that handles the task execution.
     * @return a {@link Timed} object as handle to the scheduled task
     * @throws IllegalStateException if the task could not be scheduled.
     */
    Timed schedule(long delay, TimeUnit timeUnit, Runnable task, Executor executor);

    /**
     * Schedules a task to run after an initial delay and then repeatedly at a fixed rate until
     * cancellation. The execution is handled by the given executor.
     * @param delay initial delay
     * @param timeUnitDelay delay granularity
     * @param repeat repetition delay
     * @param timeUnitRepeat repetition delay granularity
     * @param task scheduled task
     * @param executor executor that handles the task execution.
     * @return a {@link Timed} object as handle to the scheduled task
     * @throws IllegalStateException if the task could not be scheduled.
     */
    Timed schedule(long delay, TimeUnit timeUnitDelay, long repeat, TimeUnit timeUnitRepeat, Runnable task, Executor executor);

    /**
     * Schedules a message to be send after an initial delay. Will be executed by the default executor.
     * @param delay initial message delay
     * @param timeUnit delay granularity
     * @param ref target actor ref
     * @param message message object
     * @return a {@link Timed} object as handle to the scheduled task
     * @throws IllegalStateException if the task could not be scheduled.
     */
    Timed schedule(long delay, TimeUnit timeUnit, ActorRef ref, Object message);

    /**
     * Schedules a message to be send after an initial delay and then repeatedly at a fixed rate until
     * cancellation. Will be executed by the default executor.
     * @param delay initial message delay.
     * @param timeUnitDelay initial message delay granularity
     * @param repeat repetition delay
     * @param timeUnitRepeat repetition delay granularity
     * @param ref target actor ref
     * @param message message object
     * @return a {@link Timed} object as handle to the scheduled task
     * @throws IllegalStateException if the task could not be scheduled.
     */
    Timed schedule(long delay, TimeUnit timeUnitDelay, long repeat, TimeUnit timeUnitRepeat, ActorRef ref, Object message);

    /**
     * Schedules a message to be send after an initial delay. The execution is handled by the given executor.
     * @param delay message delay
     * @param timeUnit delay granularity
     * @param ref target actor ref
     * @param message message object
     * @param executor executor that handles the task execution.
     * @return a {@link Timed} object as handle to the scheduled task
     * @throws IllegalStateException if the task could not be scheduled.
     */
    Timed schedule(long delay, TimeUnit timeUnit, ActorRef ref, Object message, Executor executor);

    /**
     * Schedules a message to be send after an initial delay and then repeatedly at a fixed rate until
     * cancellation. The execution is handled by the given executor.
     * @param delay initial message delay.
     * @param timeUnitDelay initial message delay granularity
     * @param repeat repetition delay
     * @param timeUnitRepeat repetition delay granularity
     * @param ref target actor ref
     * @param message message object
     * @param executor executor that handles the task execution.
     * @return a {@link Timed} object as handle to the scheduled task
     * @throws IllegalStateException if the task could not be scheduled.
     */
    Timed schedule(long delay, TimeUnit timeUnitDelay, long repeat, TimeUnit timeUnitRepeat, ActorRef ref, Object message, Executor executor);

    /**
     * Stops the timer and releases all acquired resources. If the timer has already been
     * stopped, this method does nothing.
     *
     * Cancels all scheduled tasks.
     *
     * @return {@link Timed} objects that were cancelled by this method.
     */
    Set<Timed> stop();

}
