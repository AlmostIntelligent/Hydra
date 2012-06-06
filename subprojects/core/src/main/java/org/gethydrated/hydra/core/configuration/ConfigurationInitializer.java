package org.gethydrated.hydra.core.configuration;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class ConfigurationInitializer {

    /**
     * @var configuration.
     */
    private ConfigurationImpl cfg;

    /**
     * 
     * @param config
     *            .
     */
    public ConfigurationInitializer(final ConfigurationImpl config) {
        this.cfg = config;
    }

    /**
     * 
     * @throws ConfigItemNotFoundException .
     */
    public final void configure() throws ConfigItemNotFoundException {
        // TO-DO: xml configurator.
        BasicConfigurator.configure(cfg);
    }
}
