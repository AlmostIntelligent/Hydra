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
     * Hidden constructor to prevent instantiation.
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
        // Set standard to local only.
        cfg.setInteger("network.port", 0);
        cfg.setInteger("network.timeout-connect", 10000);
        cfg.setInteger("network.timeout-read", 0);
        cfg.setBoolean("network.keep-alive", true);

        cfg.setInteger("cli.distributed-timeout", 20);
    }
}
