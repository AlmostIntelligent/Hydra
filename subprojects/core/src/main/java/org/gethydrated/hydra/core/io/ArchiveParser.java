package org.gethydrated.hydra.core.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.jar.JarFile;

/**
 *
 */
public class ArchiveParser {

    private static final Logger logger = LoggerFactory.getLogger(ArchiveParser.class);

    public static Archive parse(Path path) throws IOException {
        try (JarFile jf = new JarFile(path.toFile())) {
            InputStream is = jf.getInputStream(jf.getEntry("HYDRA-INF/archive.xml"));
            if(is != null) {
                Archive ar = parse(is);
                if(ar != null) {
                    ar.addPath(path);
                    return ar;
                }
            }
        } catch (SAXException e) {
            logger.error("Parse Exception at file: {} : {}", path, e);
        }
        return null;
    }

    private static Archive parse(InputStream is) throws SAXException {

        final Archive ar = new Archive();

        final XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        ArchiveHandler archiveHandler = new ArchiveHandler();
        xmlReader.setContentHandler(archiveHandler);

        return archiveHandler.getResult();
    }

    private static class ArchiveHandler implements ContentHandler {

        Archive ar = null;


        @Override
        public void setDocumentLocator(Locator locator) {

        }

        @Override
        public void startDocument() throws SAXException {

        }

        @Override
        public void endDocument() throws SAXException {

        }

        @Override
        public void startPrefixMapping(String prefix, String uri) throws SAXException {

        }

        @Override
        public void endPrefixMapping(String prefix) throws SAXException {

        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            if(localName.equals("archive")) ar = new Archive();
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {

        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {

        }

        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {

        }

        @Override
        public void processingInstruction(String target, String data) throws SAXException {

        }

        @Override
        public void skippedEntity(String name) throws SAXException {

        }

        public Archive getResult() {
            return ar;
        }
    }
}
