package org.gethydrated.hydra.actors.timer;

/**
 * Internal task representation.
 */
public interface TimerTask extends Cancellable {

    /**
     * Returns the {@link Timer} that created this handle.
     * @return the {@link Timer}
     */
    Timer getTimer();

    /**
     * Returns the timestamp when this
     * task is due.
     * @return the timestamp
     */
    long getTimeStamp();

    /**
     * Increases the timestamp by a implementation
     * specific amount, to avoid collisions in sorted
     * lists or sets.
     */
    void incTimeStamp();

    /**
     * Increases the timestamp by the repetition delay.
     */
    void incRepeat();

    /**
     * Returns the repetition state of this task.
     * @return true, if this task is repeatable
     */
    boolean isRepeatable();

    /**
     * Executes the tasks. If the task is non repeatable, this
     * must set the expired flag to true.
     * @throws Exception on execution errors.
     */
    void execute() throws Exception;

    /**
     * Returns the expiry state of this task.
     * Repeatable tasks may never be expired, only cancelled.
     *
     * @return true, if the task has been executed.
     */
    boolean isExpired();
}
