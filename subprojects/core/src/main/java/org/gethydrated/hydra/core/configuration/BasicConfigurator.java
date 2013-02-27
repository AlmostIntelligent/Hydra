package org.gethydrated.hydra.core.configuration;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.config.ConfigurationImpl;

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
    public static void configure(final ConfigurationImpl cfg)
            throws ConfigItemNotFoundException {

        /* Actor configuration */
        cfg.setString("actors.test", "test");

        /* Network configuration */
        //Set standard to local only.
        cfg.setInteger("network.port", 0);

    }
}
