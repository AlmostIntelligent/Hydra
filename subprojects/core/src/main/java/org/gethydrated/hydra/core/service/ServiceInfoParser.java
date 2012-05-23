package org.gethydrated.hydra.core.service;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class ServiceInfoParser {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory
            .getLogger(ServiceInfoParser.class);

    /**
     * Parses service.xml informations.
     * 
     * @param in
     *            InputStream to parse.
     * @param expName
     *            expected service name.
     * @param expVersion
     *            expected service version.
     * @return ServiceInfo with parsed values.
     * @throws IOException
     *             on IO failures.
     */
    public static ServiceInfo parse(final InputStream in, final String expName,
            final String expVersion) throws IOException {

        final ServiceInfo si = new ServiceInfo();

        try {
            final XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setContentHandler(new ContentHandler() {

                private String currentValue;

                @Override
                public void endElement(final String uri,
                        final String localName, final String qName)
                        throws SAXException {
                    switch (localName) {
                    case "name":
                        if (expName != null && !currentValue.equals(expName)) {
                            throw new NoMatchException();
                        }
                        si.setName(currentValue);
                        break;
                    case "version":
                        if (expVersion != null
                                && !currentValue.equals(expVersion)) {
                            throw new NoMatchException();
                        }
                        si.setVersion(currentValue);
                    case "activator":
                        si.setActivator(currentValue);
                    default:
                        break;
                    }
                }

                @Override
                public void characters(final char[] ch, final int start,
                        final int length) throws SAXException {
                    currentValue = new String(ch, start, length);
                }

                @Override
                public void startElement(final String uri,
                        final String localName, final String qName,
                        final Attributes atts) throws SAXException {
                }

                @Override
                public void setDocumentLocator(final Locator locator) {
                }

                @Override
                public void startDocument() throws SAXException {
                }

                @Override
                public void endDocument() throws SAXException {
                }

                @Override
                public void startPrefixMapping(final String prefix,
                        final String uri) throws SAXException {
                }

                @Override
                public void endPrefixMapping(final String prefix)
                        throws SAXException {
                }

                @Override
                public void ignorableWhitespace(final char[] ch,
                        final int start, final int length) throws SAXException {
                }

                @Override
                public void processingInstruction(final String target,
                        final String data) throws SAXException {
                }

                @Override
                public void skippedEntity(final String name)
                        throws SAXException {
                }

            });
            xmlReader.parse(new InputSource(in));

            return si;
        } catch (final SAXException e) {
            LOG.warn(e.getMessage());
            return null;
        } catch (final NoMatchException e) {
            LOG.debug("Skipped full parsing. Name or version didnt match.");
            return null;
        }
    }

    /**
     * Exception used to skip parsing when name or version doesnt match required
     * values.
     * 
     * @author Christian Kulpa
     * @since 0.1.0
     * 
     */
    private static class NoMatchException extends RuntimeException {

        /**
         * Serialization id.
         */
        private static final long serialVersionUID = 6913269681619804532L;
    }
}
