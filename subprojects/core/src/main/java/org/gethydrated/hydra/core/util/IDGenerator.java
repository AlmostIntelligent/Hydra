package org.gethydrated.hydra.core.util;

public class IDGenerator {

    long id = 0;

    public Long getId() {
        return ++id;
    }
}
