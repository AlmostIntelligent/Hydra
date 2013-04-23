package org.gethydrated.hydra.util.xml;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 */
public class DocumentRunner {

    private final Document document;

    /**
     * Constructor.
     * @param document XML document.
     */
    public DocumentRunner(final Document document) {
        this.document = document;
    }

    /**
     * Traverses the given XML document for parser callbacks.
     * @param xmlParser xml parser.
     * @throws Exception on parsing failure.
     */
    public void traverse(final XMLParser<?> xmlParser) throws Exception {
        final Element elem = document.getDocumentElement();
        traverse(elem, xmlParser);
    }

    private void traverse(final Element element, final XMLParser<?> xmlParser)
            throws Exception {
        xmlParser.startElement(element);
        final List<Element> childs = getChildList(element);
        for (final Element e : childs) {
            traverse(e, xmlParser);
        }
        xmlParser.endElement(element);
    }

    private List<Element> getChildList(final Element element) {
        final List<Element> list = new LinkedList<>();
        final NodeList nl = element.getChildNodes();
        for (int i = 0; i < nl.getLength(); ++i) {
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                list.add((Element) nl.item(i));
            }
        }
        return list;
    }

}
