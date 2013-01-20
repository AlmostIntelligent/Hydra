package org.gethydrated.hydra.core.xml;

import org.gethydrated.hydra.core.internal.Archive;
import org.gethydrated.hydra.core.internal.Service;
import org.gethydrated.hydra.util.xml.XMLParser;
import org.w3c.dom.Element;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class ArchiveParser implements XMLParser<Archive> {

    private Archive archive;

    private boolean complete = false;

    private List<Service> services;

    private XMLParser<?> delegate = null;

    @Override
    public Archive getResult() {
        return complete ? archive : null;
    }

    @Override
    public void startElement(Element element) {
        switch (element.getTagName()) {
            case "hydra:archive":   parseArchiveStart(element);
                break;
            case "name":        parseName(element);
                break;
            case "version":     parseVersion(element);
                break;
            case "services":    parseServicesStart(element);
                break;
            case "service":     parseServiceStart(element);
            default:
                if(delegate!=null) {
                    delegate.startElement(element);
                }
        }
    }

    @Override
    public void endElement(Element element) {
        switch (element.getTagName()) {
            case "hydra:archive":   parseArchiveEnd(element);
                break;
            case "services":    parseServicesEnd(element);
                break;
            case "service":     parseServiceEnd(element);
            default:
                if (delegate!=null) {
                    delegate.endElement(element);
                }
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

    private void parseServicesStart(Element element) {
        services = new LinkedList<>();
    }

    private void parseServicesEnd(Element element) {
        for(Service s : services) {
            archive.addService(s);
        }
    }

    private void parseServiceStart(Element element) {
        delegate = new ServiceParser();
        delegate.startElement(element);
    }

    private void parseServiceEnd(Element element) {
        delegate.endElement(element);
        Service s = (Service) delegate.getResult();
        if(s != null) {
            services.add(s);
        }
        delegate = null;
    }

}
