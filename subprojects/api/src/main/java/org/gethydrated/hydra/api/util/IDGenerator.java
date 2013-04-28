package org.gethydrated.hydra.api.util;

/**
 * Simple incrementing IDGenerator.
 * 
 * This is not threadsafe.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 */
public class IDGenerator {

    private long id = 0;

    /**
     * Returns the next id.
     * @return next id.
     */
    public Long getId() {
        return ++id;
    }
}
