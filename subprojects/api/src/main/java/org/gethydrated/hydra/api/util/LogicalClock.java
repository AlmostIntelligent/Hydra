package org.gethydrated.hydra.api.util;

/**
 *
 */
public interface LogicalClock {

    void increment();

    long getCurrentTime();

    void sync(long time);

}
