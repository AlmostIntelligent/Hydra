package org.gethydrated.hydra.config.files;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.gethydrated.hydra.config.ConfigurationImpl;

/**
 * Abstract configuration writer. Writes a configuration into a stream.
 *
 * @author Hanno Sternberg
 * @since 0.1.0
 */
public abstract class ConfigurationWriter {

    /**
     * The configuration.
     */
    private ConfigurationImpl cfg;

    /**
     * Constructor.
     *
     * @param config
     *            Configuration to write.
     */
    public ConfigurationWriter(final ConfigurationImpl config) {
        cfg = config;
    }

    /**
     * @return Configuration.
     */
    public final ConfigurationImpl getCfg() {
        return cfg;
    }

    /**
     * Set a configuration to write.
     *
     * @param config
     *            The configuration.
     */
    public final void setCfg(final ConfigurationImpl config) {
        this.cfg = config;
    }

    /**
     * Save the configuration to a stream.
     *
     * @param stream
     *            The stream.
     */
    public abstract void saveToStream(PrintStream stream);

    /**
     * Saves the configuration to a file.
     *
     * @param filename
     *            The filename.
     * @throws FileNotFoundException
     *             If the file doesn't exists.
     */
    public final void saveToFile(final String filename)
            throws FileNotFoundException {
        final PrintStream stream = new PrintStream(filename);
        saveToStream(stream);
    }

}
