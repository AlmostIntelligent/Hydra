package org.gethydrated.hydra.config.files;

import java.util.LinkedList;
import java.util.List;

import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.config.ConfigurationImpl;
import org.gethydrated.hydra.util.xml.XMLParser;
import org.w3c.dom.Element;

/**
 * Configuration Parser. Creates a configuration from a XML document.
 *
 * @author Hanno Sternberg
 * @since 0.1.0
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

    /**
     * Flag to indicate, if the parsing is complete.
     */
    private boolean complete = false;

    @Override
    public final Configuration getResult() {
        return complete ? cfg : null;
    }

    @Override
    public final void startElement(final Element element) {
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
    public final void endElement(final Element element) {
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

    /**
     * Parse a configuration start tag.
     *
     * @param element
     *            The parsed element.
     */
    private void parseConfigStart(final Element element) {
        cfg = new ConfigurationImpl();
        stack = new StringStack();
    }

    /**
     * Parse a configuration end tag.
     *
     * @param element
     *            The parsed element.
     */
    private void parseConfigEnd(final Element element) {
        complete = true;
    }

    /**
     * Parse the start of a configuration list.
     *
     * @param element
     *            The parsed element.
     */
    private void parseConfigListStart(final Element element) {
        stack.push(element.getAttribute("name"));
    }

    /**
     * Parse the end of a configuration list.
     *
     * @param element
     *            The parsed element.
     */
    private void parseConfigListEnd(final Element element) {
        stack.pop();
    }

    /**
     * Parse a configuration value.
     *
     * @param element
     *            The parsed element.
     */
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
         * @param s
         *            .
         */
        public void remove(final String s) {
            strs.remove(s);
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
