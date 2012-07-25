package org.gethydrated.hydra.core.configuration.files;

import java.io.PrintStream;

import org.gethydrated.hydra.core.configuration.ConfigurationImpl;
import org.gethydrated.hydra.core.configuration.tree.ConfigList;
import org.gethydrated.hydra.core.configuration.tree.ConfigValue;
import org.gethydrated.hydra.core.configuration.tree.ConfigurationItem;

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
            for (ConfigurationItem i : list.getChilds()) {
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
        writeList(stream, getCfg().getRoot(), "");
    }

}