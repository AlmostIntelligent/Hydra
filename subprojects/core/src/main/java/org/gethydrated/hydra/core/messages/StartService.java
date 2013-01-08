package org.gethydrated.hydra.core.messages;

/**
 *
 */
public class StartService {
    private final String value;

    public StartService(String name) {
        value = name;
    }

    public String getServiceName() {
        return value;
    }
}
