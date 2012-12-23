package org.gethydrated.hydra.core.configuration.files;

import java.io.PrintStream;

import org.gethydrated.hydra.api.configuration.ConfigurationItem;
import org.gethydrated.hydra.core.configuration.ConfigurationImpl;
import org.gethydrated.hydra.core.configuration.tree.ConfigList;
import org.gethydrated.hydra.core.configuration.tree.ConfigValue;


/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class XMLConfigurationWriter extends ConfigurationWriter {

    /**
     * 
     * @param cfg
     *            Configuration.
     */
    public XMLConfigurationWriter(final ConfigurationImpl cfg) {
        super(cfg);
    }

    /**
     * 
     * @param stream
     *            .
     * @param indent
     *            Number of indentions.
     */
    protected final void writeIndent(final PrintStream stream, final int indent) {
        for (int i = 0; i < indent; i++) {
            stream.print("\t");

        }
    }

    /**
     * 
     * @param stream
     *            .
     * @param value
     *            .
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
     * 
     * @param stream
     *            .
     * @param list
     *            .
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
            for (ConfigurationItem i : list.getChildren()) {
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
     * @param stream
     *            .
     */
    public final void saveToStream(final PrintStream stream) {
        stream.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writeList(stream, (ConfigList)getCfg().getRoot(), 0);

    }

}
