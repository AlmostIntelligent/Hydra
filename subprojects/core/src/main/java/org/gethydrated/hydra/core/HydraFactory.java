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
public final class HydraFactory {
    
    /**
     * Default configuration.
     */
    private static final Configuration DEFAULTCFG = new Configuration();

    /**
     * Factory state.
     */
    private static boolean initialized = false;

    /**
     * Hide constructor to prevent instanciation.
     */
    private HydraFactory() {
    }
    
    /**
     * Creates new Hydra instance.
     * @return Hydra instance.
     */
    public static Hydra getHydra() {
        if (!initialized) {
            init();
        }
        return new HydraImpl(DEFAULTCFG);
    }

    /**
     * initializes the default configuration.
     */
    private static void init() {
        try {
            new ConfigurationInitializer(DEFAULTCFG).configure();
            initialized = true;
        } catch (ConfigItemNotFoundException e) {
            throw new IllegalStateException(
                    "Could not create hydra configuration.", e);
        }
    }
}
