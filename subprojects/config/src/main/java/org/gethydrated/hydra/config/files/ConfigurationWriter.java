package org.gethydrated.hydra.config.files;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.gethydrated.hydra.config.ConfigurationImpl;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public abstract class ConfigurationWriter {

    /**
     * @var The configuration.
     */
    private ConfigurationImpl cfg;

    /**
     * Constructor.
     * 
     * @param config
     *            .
     */
    public ConfigurationWriter(final ConfigurationImpl config) {
        cfg = config;
    }

    /**
     * 
     * @return Configuration.
     */
    public final ConfigurationImpl getCfg() {
        return cfg;
    }

    /**
     * 
     * @param config
     *            .
     */
    public final void setCfg(final ConfigurationImpl config) {
        this.cfg = config;
    }

    /**
     * 
     * @param stream
     *            .
     */
    public abstract void saveToStream(PrintStream stream);

    /**
     * Saves the configuration to a file.
     * 
     * @param filename
     *            .
     * @throws FileNotFoundException .
     */
    public final void saveToFile(final String filename)
            throws FileNotFoundException {
        final PrintStream stream = new PrintStream(filename);
        saveToStream(stream);
    }

}
