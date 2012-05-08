package org.gethydrated.hydra.core.config;

import java.io.PrintStream;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 *
 */
public class PlainConfigurationWriter extends ConfigurationWriter {

	public PlainConfigurationWriter(Configuration _cfg) {
		super(_cfg);
	}

	protected void writeValue(PrintStream stream, ConfigValue<?> value,
			String namePrefix) {
		stream.print(namePrefix);
		stream.print(value.getName());
		stream.print("=");
		stream.println(value.value());
		;

	}

	protected void writeList(PrintStream stream, ConfigList list,
			String namePrefix) {
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

	@Override
	public void saveToStream(PrintStream stream) {
		writeList(stream, cfg.getRoot(), "");
	}

}
