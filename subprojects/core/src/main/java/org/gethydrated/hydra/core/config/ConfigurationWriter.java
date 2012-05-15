package org.gethydrated.hydra.core.config;

import java.io.FileNotFoundException;
import java.io.PrintStream;

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
    private Configuration cfg;

    /**
     * Constructor.
     * @param config .
     */
    public ConfigurationWriter(final Configuration config) {
        cfg = config;
    }

    /**
     * 
     * @return Configuration.
     */
    public final Configuration getCfg() {
        return cfg;
    }

    /**
     * 
     * @param config .
     */
    public final void setCfg(final Configuration config) {
        this.cfg = config;
    }

    /**
     * 
     * @param stream .
     */
    public abstract void saveToStream(PrintStream stream);

    /**
     * Saves the configuration to a file.
     * @param filename .
     * @throws FileNotFoundException .
     */
    public final void saveToFile(final String filename) throws FileNotFoundException {
        PrintStream stream = new PrintStream(filename);
        saveToStream(stream);
    }

}
