package org.gethydrated.hydra.util.xml;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Document loader.
 */
public interface DocumentLoader {

    /**
     * Loads an XML document.
     * @param inputStream xml input stream.
     * @return XML document.
     * @throws ParserConfigurationException on failure.
     * @throws IOException on failure.
     * @throws SAXException on failure.
     */
    Document loadDocument(InputStream inputStream)
            throws ParserConfigurationException, IOException, SAXException;

}
