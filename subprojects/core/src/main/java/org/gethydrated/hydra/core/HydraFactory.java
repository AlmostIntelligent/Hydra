package org.gethydrated.hydra.core;

import org.gethydrated.hydra.api.Hydra;
import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.api.service.deploy.ServiceResolver;
import org.gethydrated.hydra.config.ConfigurationImpl;
import org.gethydrated.hydra.core.configuration.ConfigurationInitializer;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 */
public final class HydraFactory {

    /**
     * Default configuration.
     */
    private static final ConfigurationImpl DEFAULTCFG = new ConfigurationImpl();

    private Set<ServiceResolver> resolvers = new HashSet<>();

    /**
     * Factory state.
     */
    private static boolean initialized = false;

    /**
     * Hide constructor to prevent instantiation.
     */
    private HydraFactory() {
    }

    /**
     * Creates new Hydra instance.
     * 
     * @return Hydra instance.
     * @throws HydraException on failure.
     */
    public Hydra getHydra() throws HydraException {
        return new HydraImpl(DEFAULTCFG, resolvers);
    }

    public void addResolver(ServiceResolver resolver) {
        resolvers.add(resolver);
    }

    public static HydraFactory create() {
        if (!initialized) {
            init();
        }
        return new HydraFactory();
    }

    /**
     * initializes the default configuration.
     */
    private static void init() {
        try {
            new ConfigurationInitializer(DEFAULTCFG).configure();
            initialized = true;
        } catch (final ConfigItemNotFoundException e) {
            throw new IllegalStateException(
                    "Could not create hydra configuration.", e);
        }
    }
}
