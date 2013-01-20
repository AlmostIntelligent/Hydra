package org.gethydrated.hydra.core.xml;

import org.gethydrated.hydra.core.io.Archive;
import org.gethydrated.hydra.util.xml.XMLParser;
import org.w3c.dom.Element;

/**
 *
 */
public class ArchiveParser implements XMLParser<Archive> {

    Archive archive;

    boolean complete = false;

    @Override
    public Archive getResult() {
        return complete ? archive : null;
    }

    @Override
    public void startElement(Element element) {
        switch (element.getTagName()) {
            case "hydra:archive":   parseArchiveStart(element);
                break;
            case "name":    parseName(element);
                break;
            case "version": parseVersion(element);
        }
    }

    @Override
    public void endElement(Element element) {
        switch (element.getTagName()) {
            case "hydra:archive":   parseArchiveEnd(element);
        }
    }

    private void parseArchiveStart(Element element) {
        archive = new Archive();
    }

    private void parseArchiveEnd(Element element) {
        complete = true;
    }

    private void parseName(Element element) {
        archive.setName(element.getTextContent());
    }

    private void parseVersion(Element element) {
        archive.setVersion(element.getTextContent());
    }

}
