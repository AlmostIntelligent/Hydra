package org.gethydrated.hydra.actors.scheduling;

/**
 *  A facility for threads to schedule tasks for future execution in a background thread.
 *  Tasks may be scheduled for one-time execution, or for repeated execution at regular intervals.
 */
public interface Timer {

    Cancellable schedule();

}
