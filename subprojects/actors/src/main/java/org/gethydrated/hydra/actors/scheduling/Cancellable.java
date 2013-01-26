package org.gethydrated.hydra.actors.scheduling;

/**
 * A Task that can be cancelled. Cancellation is performed by
 * the cancel method.
 *
 * @since 0.2.0
 * @author Christian Kulpa
 *
 */
public interface Cancellable {

    /**
     * Cancels the execution of the task. If the task has already
     * begun or finished execution this method will fail.
     * @return false, if the task could not be cancelled, typically because
     * it already started or completed execution.
     */
    boolean cancel();

    /**
     * Returns if this task was cancelled before execution.
     * @return true if this task was cancelled before execution.
     */
    boolean isCancelled();

}
