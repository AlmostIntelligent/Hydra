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

    protected Configuration cfg;

    public ConfigurationWriter(Configuration _cfg) {
        cfg = _cfg;
    }

    public abstract void saveToStream(PrintStream stream);

    public void saveToFile(String filename) throws FileNotFoundException {
        PrintStream stream = new PrintStream(filename);
        saveToStream(stream);
    }

}
