package org.gethydrated.hydra.core.concurrent;

import org.gethydrated.hydra.api.event.SystemEvent;

/**
 * LockGranted message.
 */
public class LockGranted implements SystemEvent {

    public LockGranted() {
    }

    @Override
    public String toString() {
        return "LockGranted{}";
    }
}
