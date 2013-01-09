package org.gethydrated.hydra.core.configuration;

import java.io.IOException;
import java.util.Map;

import org.gethydrated.hydra.config.ConfigurationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.config.files.XMLConfigurationReader;


/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class ConfigurationInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);

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


    private final void configureBasic() throws ConfigItemNotFoundException {
        BasicConfigurator.configure(cfg);
    }

    public final void configure()throws ConfigItemNotFoundException  {
        configureBasic();
    }

    public final void configure(Map<String, Object> configs) throws ConfigItemNotFoundException {
        configureBasic();
        for (String k : configs.keySet()) {
            Object val = configs.get(k);
            if (val.getClass().equals(String.class)) {
                    cfg.setString(k,(String) val);
            } else if (val.getClass().equals(Integer.class)) {
                cfg.setInteger(k, (Integer) val);
            } else if (val.getClass().equals(Double.class)) {
                cfg.setFloat(k, (Double) val);
            } else if (val.getClass().equals(Boolean.class)) {
                cfg.setBoolean(k, (Boolean) val);
            } else {
                LOG.error("Unknown value type for key {}: {}", k, val.getClass().toString());
            }
        }
    }


    /**
     * Perform a two step configuration
     * 1. Set basic configuration based on default configuration definitions
     * 2. Load and set user specific configuration
     *
     * @throws ConfigItemNotFoundException .
     */
    public final void configure(String configurationFile) throws ConfigItemNotFoundException {
        try {
            configureBasic();
            final XMLConfigurationReader rdr = new XMLConfigurationReader();
            final Configuration usrCfg = rdr.load(configurationFile);
            cfg = (ConfigurationImpl) ConfigMerger.merge(cfg, usrCfg);
        } catch (SAXException e) {
            LOG.error("Error while parsing the configuration file.");
            // Todo: Add fallback config generation
        } catch (IOException e) {
            LOG.error("Invalid configuration file.");
            // Todo: Add fallback config generation
        } catch (ParserConfigurationException e) {
            LOG.error("Invalid parser configuration.");
            // Todo: Add fallback config generation
        }
    }
}
