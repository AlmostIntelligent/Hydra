package org.gethydrated.hydra.core.xml;

import java.util.LinkedList;
import java.util.List;

import org.gethydrated.hydra.core.internal.Archive;
import org.gethydrated.hydra.core.internal.Service;
import org.gethydrated.hydra.util.xml.XMLParser;
import org.w3c.dom.Element;

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
    public void startElement(final Element element) {
        switch (element.getTagName()) {
        case "hydra:archive":
            parseArchiveStart(element);
            break;
        case "name":
            parseName(element);
            break;
        case "version":
            parseVersion(element);
            break;
        case "services":
            parseServicesStart(element);
            break;
        case "service":
            parseServiceStart(element);
            break;
        default:
            if (delegate != null) {
                delegate.startElement(element);
            }
        }
    }

    @Override
    public void endElement(final Element element) {
        switch (element.getTagName()) {
        case "hydra:archive":
            parseArchiveEnd(element);
            break;
        case "services":
            parseServicesEnd(element);
            break;
        case "service":
            parseServiceEnd(element);
            break;
        default:
            if (delegate != null) {
                delegate.endElement(element);
            }
        }
    }

    private void parseArchiveStart(final Element element) {
        archive = new Archive();
    }

    private void parseArchiveEnd(final Element element) {
        complete = true;
    }

    private void parseName(final Element element) {
        if (delegate == null) {
            archive.setName(element.getTextContent());
        } else {
            delegate.startElement(element);
        }
    }

    private void parseVersion(final Element element) {
        if (delegate == null) {
            archive.setVersion(element.getTextContent());
        } else {
            delegate.startElement(element);
        }
    }

    private void parseServicesStart(final Element element) {
        services = new LinkedList<>();
    }

    private void parseServicesEnd(final Element element) {
        for (final Service s : services) {
            archive.addService(s);
        }
    }

    private void parseServiceStart(final Element element) {
        delegate = new ServiceParser();
        delegate.startElement(element);
    }

    private void parseServiceEnd(final Element element) {
        delegate.endElement(element);
        final Service s = (Service) delegate.getResult();
        if (s != null) {
            services.add(s);
        }
        delegate = null;
    }

}
