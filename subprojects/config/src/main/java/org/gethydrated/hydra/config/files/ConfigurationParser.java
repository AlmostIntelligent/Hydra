package org.gethydrated.hydra.config.files;

import java.util.LinkedList;
import java.util.List;

import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.config.ConfigurationImpl;
import org.gethydrated.hydra.util.xml.XMLParser;
import org.w3c.dom.Element;

/**
 * Configuration parser.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 */
public class ConfigurationParser implements XMLParser<Configuration> {

    /**
     * @var The configuration.
     */
    private Configuration cfg;

    /**
     * @var Configuration hierarchy as a stack.
     */
    private StringStack stack;

    private boolean complete = false;

    @Override
    public Configuration getResult() {
        return complete ? cfg : null;
    }

    @Override
    public void startElement(final Element element) {
        switch (element.getTagName()) {
        case "hydra:configuration":
            parseConfigStart(element);
            break;
        case "configlist":
            parseConfigListStart(element);
            break;
        case "configvalue":
            parseConfigValue(element);
        default:
            break;
        }
    }

    @Override
    public void endElement(final Element element) {
        switch (element.getTagName()) {
        case "hydra:configuration":
            parseConfigEnd(element);
            break;
        case "configlist":
            parseConfigListEnd(element);
            break;
        default:
            break;
        }
    }

    private void parseConfigStart(final Element element) {
        cfg = new ConfigurationImpl();
        stack = new StringStack();
    }

    private void parseConfigEnd(final Element element) {
        complete = true;
    }

    private void parseConfigListStart(final Element element) {
        stack.push(element.getAttribute("name"));
    }

    private void parseConfigListEnd(final Element element) {
        stack.pop();
    }

    private void parseConfigValue(final Element element) {

        final String value = element.getAttribute("value");
        if (!value.trim().isEmpty()) {
            String name = stack
                    .toString(ConfigurationImpl.getConfigSeparator());
            name = name.trim().isEmpty() ? element.getAttribute("name") : name
                    + ConfigurationImpl.getConfigSeparator()
                    + element.getAttribute("name");
            if (value.equalsIgnoreCase("TRUE")) {
                cfg.setBoolean(name, true);
            } else if (value.equalsIgnoreCase("FALSE")) {
                cfg.setBoolean(name, false);
            } else {
                try {
                    final int i = Integer.parseInt(value);
                    cfg.setInteger(name, i);
                } catch (final Exception e) {
                    try {
                        final double d = Double.parseDouble(value);
                        cfg.setFloat(name, d);
                    } catch (final Exception ee) {
                        cfg.setString(name, value);
                    }
                }
            }
        }
    }

    /**
     * A stack of strings.
     * 
     * @author Hanno
     * @since 0.1.0
     */
    private class StringStack {

        /**
         * @var List structure for the stack.
         */
        private final List<String> strs = new LinkedList<>();

        /**
         * 
         * @param s
         *            .
         */
        public void push(final String s) {
            strs.add(s);
        }

        /**
         *
         */
        public void pop() {
            strs.remove(strs.size() - 1);
        }

        /**
         * 
         * @param seperator
         *            .
         * @return The stack as a single string.
         */
        public String toString(final String seperator) {
            final StringBuilder sb = new StringBuilder();
            String str;
            for (final String s : strs) {
                sb.append(s);
                sb.append(seperator);
            }
            str = sb.toString();
            if (str.endsWith(seperator)) {
                str = str.substring(0, str.length() - seperator.length());
            }
            return str;
        }

    }
}
