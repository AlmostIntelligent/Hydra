package org.gethydrated.hydra.core.registry;

/**
 * Unregister message.
 */
public class UnregisterService {
    private final String name;

    /**
     * Constructor.
     * @param name Service name.
     */
    public UnregisterService(final String name) {
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
