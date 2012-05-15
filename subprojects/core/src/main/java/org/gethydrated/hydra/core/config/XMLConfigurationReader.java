package org.gethydrated.hydra.core.config;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class XMLConfigurationReader {

    /**
     * A stack of strings.
     * 
     * @author Hanno
     * @since 0.1.0
     */
    class StringStack {

        /**
         * @var List structure for the stack.
         */
        private List<String> strs = new LinkedList<String>();

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
            StringBuilder sb = new StringBuilder();
            String str;
            for (String s : strs) {
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

    /**
     * @var Configuration hierarchy as a stack.
     */
    private StringStack stack;
    /**
     * @var The configuration.
     */
    private Configuration cfg;

    /**
     * 
     * @param filename
     *            filename.
     * @return Configuration.
     * @throws SAXException .
     * @throws IOException .
     * @throws ParserConfigurationException .
     */
    public final Configuration load(final String filename) throws SAXException,
            IOException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        cfg = new Configuration();
        stack = new StringStack();

        DefaultHandler handler = new DefaultHandler() {

            /**
             * @param uri
             *            .
             * @param localname
             *            .
             * @param qName
             *            .
             * @param attributes
             *            .
             * @throws SAXExcpetion .
             */
            public void startElement(final String uri, final String localName,
                    final String qName, final Attributes attributes)
                    throws SAXException {
                if (!qName.equalsIgnoreCase("configuration")) {
                    stack.push(qName);
                }
            }

            /**
             * @param uri
             *            .
             * @param localname
             *            .
             * @param qName
             *            .
             */
            public void endElement(final String uri, final String localName,
                    final String qName) throws SAXException {
                if (!qName.equalsIgnoreCase("configuration")) {
                    stack.pop();
                }
            }

            /**
             * @param ch
             *            Char array.
             * @param start
             *            start index.
             * @param length
             *            array length.
             */
            public void characters(final char[] ch, final int start,
                    final int length) throws SAXException {
                String s = new String(ch, start, length);
                if (!s.trim().isEmpty()) {
                    /* Boolean */
                    if (s.equalsIgnoreCase("TRUE")) {
                        cfg.setBoolean(
                                stack.toString(Configuration.getConfigSeparator()),
                                true);
                    } else if (s.equalsIgnoreCase("FALSE")) {
                        cfg.setBoolean(
                                stack.toString(Configuration.getConfigSeparator()),
                                false);
                    } else {
                        /* Integer */
                        try {
                            int i = Integer.parseInt(s);
                            cfg.setInteger(stack
                                    .toString(Configuration.getConfigSeparator()),
                                    i);
                        } catch (Exception e) {
                            try {
                                /* Double */
                                Double d = Double.parseDouble(s);
                                cfg.setFloat(
                                        stack.toString(Configuration.getConfigSeparator()),
                                        d);
                            } catch (Exception ee) {
                                /* String */
                                cfg.setString(
                                        stack.toString(Configuration.getConfigSeparator()),
                                        s);
                            }
                        }
                    }
                }
            }

        };

        saxParser.parse(filename, handler);

        return cfg;
    }

}
