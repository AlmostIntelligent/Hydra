package org.gethydrated.hydra.config.files;

import java.io.PrintStream;

import org.gethydrated.hydra.api.configuration.ConfigurationItem;
import org.gethydrated.hydra.config.ConfigurationImpl;
import org.gethydrated.hydra.config.tree.ConfigList;
import org.gethydrated.hydra.config.tree.ConfigValue;

/**
 * Configuration Writer for plain text files.
 *
 * @author Hanno Sternberg
 * @since 0.1.0
 *
 */
public class PlainConfigurationWriter extends ConfigurationWriter {

    /**
     * Constructor.
     *
     * @param cfg
     *            The configuration to write.
     */
    public PlainConfigurationWriter(final ConfigurationImpl cfg) {
        super(cfg);
    }

    /**
     * Write a value to the stream.
     *
     * @param stream
     *            The stream.
     * @param value
     *            The value.
     * @param namePrefix
     *            The name prefix.
     */
    protected final void writeValue(final PrintStream stream,
            final ConfigValue<?> value, final String namePrefix) {
        stream.print(namePrefix);
        stream.print(value.getName());
        stream.print("=");
        stream.println(value.value());

    }

    /**
     * Write a list to the stream.
     *
     * @param stream
     *            The stream.
     * @param list
     *            The list.
     * @param namePrefix
     *            The name prefix.
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
     * Save the configuration to a stream.
     *
     * @param stream
     *            The stream.
     */
    @Override
    public final void saveToStream(final PrintStream stream) {
        writeList(stream, (ConfigList) getCfg().getRoot(), "");
    }

}
