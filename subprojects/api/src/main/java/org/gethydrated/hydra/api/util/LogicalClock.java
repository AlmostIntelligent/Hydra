package org.gethydrated.hydra.api.util;

/**
 * Logical clock interface.
 */
public interface LogicalClock {

    /**
     * Increments the clocks value.
     */
    void increment();

    /**
     * Returns the actual time.
     * @return time.
     */
    long getCurrentTime();

    /**
     * Synchronizes to a given timestamp.
     * @param time timestamp.
     */
    void sync(long time);

}
