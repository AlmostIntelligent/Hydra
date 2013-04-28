package org.gethydrated.hydra.util.xml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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
