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

    class StringStack {
        List<String> strs = new LinkedList<String>();

        public void push(String s) {
            strs.add(s);
        }

        public void pop() {
            strs.remove(strs.size() - 1);
        }

        public void remove(String s) {
            strs.remove(s);
        }

        public String toString(String seperator) {
            StringBuilder sb = new StringBuilder();
            String str;
            for (String s : strs) {
                sb.append(s);
                sb.append(seperator);
            }
            str = sb.toString();
            if (str.endsWith(seperator))
                str = str.substring(0, str.length() - seperator.length());
            return str;
        }

    }

    StringStack stack;
    Configuration cfg;

    public Configuration load(String filename) throws SAXException,
            IOException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        cfg = new Configuration();
        stack = new StringStack();

        DefaultHandler handler = new DefaultHandler() {

            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) throws SAXException {
                if (!qName.equalsIgnoreCase("configuration"))
                    stack.push(qName);
            }

            public void endElement(String uri, String localName, String qName)
                    throws SAXException {
                if (!qName.equalsIgnoreCase("configuration"))
                    stack.pop();
            }

            public void characters(char ch[], int start, int length)
                    throws SAXException {
                String s = new String(ch, start, length);
                if (!s.trim().isEmpty()) {
                    /* Boolean */
                    if (s.equalsIgnoreCase("TRUE"))
                        cfg.setBoolean(
                                stack.toString(Configuration.CONFIG_SEPERATOR),
                                true);
                    else if (s.equalsIgnoreCase("FALSE"))
                        cfg.setBoolean(
                                stack.toString(Configuration.CONFIG_SEPERATOR),
                                false);
                    else {
                        /* Integer */
                        try {
                            int i = Integer.parseInt(s);
                            cfg.setInteger(stack
                                    .toString(Configuration.CONFIG_SEPERATOR),
                                    i);
                        } catch (Exception e) {
                            try {
                                /* Double */
                                Double d = Double.parseDouble(s);
                                cfg.setFloat(
                                        stack.toString(Configuration.CONFIG_SEPERATOR),
                                        d);
                            } catch (Exception ee) {
                                /* String */
                                cfg.setString(
                                        stack.toString(Configuration.CONFIG_SEPERATOR),
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
