package org.gethydrated.hydra.config.files;

import java.io.PrintStream;

import org.gethydrated.hydra.api.configuration.ConfigurationItem;
import org.gethydrated.hydra.config.ConfigurationImpl;
import org.gethydrated.hydra.config.tree.ConfigList;
import org.gethydrated.hydra.config.tree.ConfigValue;

/**
 * Configuration Write for XML document.
 *
 * @author Hanno Sternberg
 * @since 0.1.0
 */
public class XMLConfigurationWriter extends ConfigurationWriter {

    /**
     * Constructor.
     *
     * @param cfg
     *            The Configuration to write.
     */
    public XMLConfigurationWriter(final ConfigurationImpl cfg) {
        super(cfg);
    }

    /**
     * Write a number of indention characters to a stream.
     *
     * @param stream
     *            The Stream.
     * @param indent
     *            Number of indention.
     */
    protected final void writeIndent(final PrintStream stream, final int indent) {
        for (int i = 0; i < indent; i++) {
            stream.print("\t");

        }
    }

    /**
     * Write a value to the stream.
     *
     * @param stream
     *            The Stream.
     * @param value
     *            The value.
     * @param indent
     *            Indention.
     */
    protected final void writeValue(final PrintStream stream,
            final ConfigValue<?> value, final int indent) {
        writeIndent(stream, indent);
        stream.print("<");
        stream.print(value.getName());
        stream.print(">");
        stream.print(value.value());
        stream.print("</");
        stream.print(value.getName());
        stream.println(">");

    }

    /**
     * Write a list to the stream.
     *
     * @param stream
     *            The Streams.
     * @param list
     *            The list.
     * @param indent
     *            Indention.
     */
    protected final void writeList(final PrintStream stream,
            final ConfigList list, final int indent) {
        if (list.hasChildren()) {
            writeIndent(stream, indent);
            stream.print("<");
            stream.print(list.getName());
            stream.println(">");
            for (final ConfigurationItem i : list.getChildren()) {
                if (i.hasValue()) {
                    writeValue(stream, (ConfigValue<?>) i, indent + 1);
                } else if (i.hasChildren()) {
                    writeList(stream, (ConfigList) i, indent + 1);
                }
            }
            writeIndent(stream, indent);
            stream.print("</");
            stream.print(list.getName());
            stream.println(">");
        }
    }

    /**
     * Save the stream.
     *
     * @param stream
     *            The stream.
     */
    @Override
    public final void saveToStream(final PrintStream stream) {
        stream.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writeList(stream, (ConfigList) getCfg().getRoot(), 0);

    }

}
