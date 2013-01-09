package org.gethydrated.hydra.core.service;

/**
 * Simple counting id generator.
 */
public class IDGenerator {

    private long id = 0;

    public Long getId() {
        return id++;
    }
}
