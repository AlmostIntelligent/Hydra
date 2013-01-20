package org.gethydrated.hydra.util.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class DocumentRunner {

    private final Document document;

    public DocumentRunner(Document document) {
        this.document = document;
    }

    public void traverse(XMLParser<?> xmlParser) throws Exception {
        Element elem = document.getDocumentElement();
        traverse(elem, xmlParser);
    }

    private void traverse(Element element, XMLParser<?> xmlParser) throws Exception {
        xmlParser.startElement(element);
        List<Element> childs = getChildList(element);
        for(Element e: childs) {
            traverse(e, xmlParser);
        }
        xmlParser.endElement(element);
    }

    private List<Element> getChildList(Element element) {
        List<Element> list = new LinkedList<>();
        NodeList nl = element.getChildNodes();
        for (int i = 0; i < nl.getLength(); ++i) {
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE)
                list.add((Element) nl.item(i));
        }
        return list;
    }

}
