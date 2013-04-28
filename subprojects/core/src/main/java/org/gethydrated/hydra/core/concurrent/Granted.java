package org.gethydrated.hydra.core.concurrent;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Lock granted.
 */
public class Granted {

    private final AtomicBoolean valid;
    /**
     * Constructor.
     * @param b validation.
     */
    public Granted(AtomicBoolean b) {
        valid = b;
    }

    /**
     * Returns if the Grant is still valid.
     * @return true if valid.
     */
    public boolean isValid() {
        return valid.get();
    }
}
