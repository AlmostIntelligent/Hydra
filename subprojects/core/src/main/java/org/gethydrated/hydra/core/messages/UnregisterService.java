package org.gethydrated.hydra.core.messages;

/**
 *
 */
public class UnregisterService {
    private final String name;

    public UnregisterService(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
