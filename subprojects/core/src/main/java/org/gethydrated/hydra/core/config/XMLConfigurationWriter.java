package org.gethydrated.hydra.core.config;

import java.io.PrintStream;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 *
 */
public class XMLConfigurationWriter extends ConfigurationWriter {

	public XMLConfigurationWriter(Configuration _cfg) {
		super(_cfg);
	}

	protected void writeIndent(PrintStream stream, int indent) {
		for (int i = 0; i < indent; i++)
			stream.print("\t");
	}

	protected void writeValue(PrintStream stream, ConfigValue<?> value,
			int indent) {
		writeIndent(stream, indent);
		stream.print("<");
		stream.print(value.getName());
		stream.print(">");
		stream.print(value.value());
		stream.print("</");
		stream.print(value.getName());
		stream.println(">");

	}

	protected void writeList(PrintStream stream, ConfigList list, int indent) {
		if (list.hasChildren()) {
			writeIndent(stream, indent);
			stream.print("<");
			stream.print(list.getName());
			stream.println(">");
			for (ConfigurationItem i : list.getChilds()) {
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

	public void saveToStream(PrintStream stream) {
		stream.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		writeList(stream, cfg.getRoot(), 0);

	}

}
