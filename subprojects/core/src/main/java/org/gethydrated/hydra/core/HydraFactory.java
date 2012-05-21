package org.gethydrated.hydra.core;

import org.gethydrated.hydra.api.Hydra;
import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.core.config.Configuration;
import org.gethydrated.hydra.core.config.ConfigurationInitializer;

/**
 * 
 * @author chris
 * @since 0.1.0
 */
public class HydraFactory {

    private static final Configuration defaultCfg = new Configuration();

    private static boolean initialized = false;

    public static Hydra getHydra() {
        if (!initialized) {
            init();
        }
        return new HydraImpl(defaultCfg);
    }

    private static void init() {
        try {
            new ConfigurationInitializer(defaultCfg).configure();
            initialized = true;
        } catch (ConfigItemNotFoundException e) {
            throw new IllegalStateException(
                    "Could not create hydra configuration.", e);
        }
    }
}
