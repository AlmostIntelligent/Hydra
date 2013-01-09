package org.gethydrated.hydra.api.util;

public class IDGenerator {

    long id = 0;

    public Long getId() {
        return ++id;
    }
}
