package org.gethydrated.hydra.util.xml;

import org.w3c.dom.Element;

/**
 * Generic parser interface used by {@link AbstractXMLReader} to handle custom
 * parse results.
 * 
 * Context must be hold by implementing classes as needed.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 * @param <T> parse result type.
 */
public interface XMLParser<T> {
    /**
     * Is invoked after the XML tree has been visited completely.
     * 
     * @return Parse result.
     */
    T getResult();

    /**
     * Start element hook.
     * 
     * @param element Current element.
     */
    void startElement(Element element);

    /**
     * End element hook.
     * 
     * @param element Current element.
     */
    void endElement(Element element);
}
