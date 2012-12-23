package org.gethydrated.hydra.core.configuration;

import java.io.IOException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.core.configuration.files.XMLConfigurationReader;


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
     * Perform a two step configuration
     * 1. Set basic configuration based on default configuration definitions
     * 2. Load and set user specific configuration
     *
     * @throws ConfigItemNotFoundException .
     */
    public final void configure(String configurationFile) throws ConfigItemNotFoundException {
        try {
            final XMLConfigurationReader rdr = new XMLConfigurationReader();
            final Configuration usrCfg = rdr.load(configurationFile);
            BasicConfigurator.configure(cfg);
        } catch (SAXException e) {

        } catch (IOException e) {

        } catch (ParserConfigurationException e) {

        }
    }
}
