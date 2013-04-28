package org.gethydrated.hydra.core.service;

/**
 * Service start message.
 */
public class StartService {
    private final String name;

    /**
     * Constructor.
     * @param name service name.
     */
    public StartService(final String name) {
        this.name = name;
    }
    
    /**
     * Returns the service name.
     * @return service name.
     */
    public String getName() {
        return name;
    }
}
