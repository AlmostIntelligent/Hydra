package org.gethydrated.hydra.core.config;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;

/**
 * Configures basic settings.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 *
 */
public final class BasicConfigurator {
    
    /**
     * Hide constructor to prevent instanciation.
     */
    private BasicConfigurator() {
    }

    /**
     * 
     * @param cfg
     *            Configuration .
     * @throws ConfigItemNotFoundException .
     */
    public static void configure(final Configuration cfg)
            throws ConfigItemNotFoundException {
        // TO-DO: initialize standard config values
    }
}
