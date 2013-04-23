package org.gethydrated.hydra.config.files;

import java.io.PrintStream;

import org.gethydrated.hydra.api.configuration.ConfigurationItem;
import org.gethydrated.hydra.config.ConfigurationImpl;
import org.gethydrated.hydra.config.tree.ConfigList;
import org.gethydrated.hydra.config.tree.ConfigValue;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class PlainConfigurationWriter extends ConfigurationWriter {

    /**
     * 
     * @param cfg
     *            .
     */
    public PlainConfigurationWriter(final ConfigurationImpl cfg) {
        super(cfg);
    }

    /**
     * 
     * @param stream
     *            .
     * @param value
     *            .
     * @param namePrefix
     *            .
     */
    protected final void writeValue(final PrintStream stream,
            final ConfigValue<?> value, final String namePrefix) {
        stream.print(namePrefix);
        stream.print(value.getName());
        stream.print("=");
        stream.println(value.value());

    }

    /**
     * 
     * @param stream
     *            .
     * @param list
     *            .
     * @param namePrefix
     *            .
     */
    protected final void writeList(final PrintStream stream,
            final ConfigList list, final String namePrefix) {
        if (list.hasChildren()) {
            for (final ConfigurationItem i : list.getChildren()) {
                if (i.hasValue()) {
                    writeValue(stream, (ConfigValue<?>) i,
                            namePrefix + list.getName() + ".");
                } else if (i.hasChildren()) {
                    writeList(stream, (ConfigList) i,
                            namePrefix + list.getName() + ".");
                }
            }

        }
    }

    /**
     * @param stream
     *            .
     */
    @Override
    public final void saveToStream(final PrintStream stream) {
        writeList(stream, (ConfigList) getCfg().getRoot(), "");
    }

}
