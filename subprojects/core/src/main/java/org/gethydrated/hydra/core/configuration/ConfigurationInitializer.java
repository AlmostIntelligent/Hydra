package org.gethydrated.hydra.core.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.config.ConfigurationImpl;
import org.gethydrated.hydra.config.files.XMLConfigurationReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class ConfigurationInitializer {

    private static final Logger LOG = LoggerFactory
            .getLogger(Configuration.class);

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

    private void configureBasic() throws ConfigItemNotFoundException {
        BasicConfigurator.configure(cfg);
    }

    /**
     * 
     * @throws ConfigItemNotFoundException on failure.
     */
    public final void configure() throws ConfigItemNotFoundException {
        configureBasic();
    }

    /**
     * 
     * @param configs configs
     * @throws ConfigItemNotFoundException on failure
     */
    public final void configure(final Map<String, Object> configs)
            throws ConfigItemNotFoundException {
        configureBasic();
        for (final String k : configs.keySet()) {
            final Object val = configs.get(k);
            if (val.getClass().equals(String.class)) {
                cfg.setString(k, (String) val);
            } else if (val.getClass().equals(Integer.class)) {
                cfg.setInteger(k, (Integer) val);
            } else if (val.getClass().equals(Double.class)) {
                cfg.setFloat(k, (Double) val);
            } else if (val.getClass().equals(Boolean.class)) {
                cfg.setBoolean(k, (Boolean) val);
            } else {
                LOG.error("Unknown value type for key {}: {}", k, val
                        .getClass().toString());
            }
        }
    }

    /**
     * Perform a two step configuration 1. Set basic configuration based on
     * default configuration definitions 2. Load and set user specific
     * configuration
     * 
     * @param configurationFile config file.
     * @throws ConfigItemNotFoundException .
     */
    public final void configure(final String configurationFile)
            throws ConfigItemNotFoundException {
        configureBasic();
        final XMLConfigurationReader rdr = new XMLConfigurationReader();
        final InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream(configurationFile);
        final Configuration usrCfg = rdr.parse(inputStream);
        if (usrCfg != null) {
            cfg = (ConfigurationImpl) ConfigMerger.merge(cfg, usrCfg);
        }
        try {
            inputStream.close();
        } catch (final IOException e) {
            LOG.error("An IO error occured.", e);
        }
    }
}
