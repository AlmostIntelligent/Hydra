package org.gethydrated.hydra.util.xml;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public interface DocumentLoader {

    Document loadDocument(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException;

}
