package org.gethydrated.hydra.actors;

import org.gethydrated.hydra.api.util.LogicalClock;

/**
 * Simple logical clock based ob lamports logical clock algorithm.
 */
public class LamportsClock implements LogicalClock {

    private long count = 0;

    @Override
    public synchronized void increment() {
        count++;
    }

    @Override
    public synchronized long getCurrentTime() {
        return count;
    }

    @Override
    public synchronized void sync(long time) {
        if(count < time) {
            count = time++;
        }
    }
}
