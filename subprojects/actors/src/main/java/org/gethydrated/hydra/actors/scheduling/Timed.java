package org.gethydrated.hydra.actors.scheduling;

/**
 *
 */
public interface Timed extends Cancellable {

    Timer getTimer();

    TimerTask getTask();

    boolean isExpired();
}
