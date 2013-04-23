package org.gethydrated.hydra.actors.timer;

/**
 * Handle for a task that was successfully scheduled by a {@link Timer}.
 */
public interface Timeout extends Cancellable {

    /**
     * Returns the {@link Timer} that created this handle.
     * 
     * @return the {@link Timer}
     */
    Timer getTimer();

    /**
     * Returns the {@link TimerTask} that is associated to this handle.
     * 
     * @return the associated {@link TimerTask}
     */
    TimerTask getTask();

    /**
     * Returns the expiry state for the underlying {@link TimerTask}.
     * 
     * @return true, if task has been expired.
     */
    boolean isExpired();
}
