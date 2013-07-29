package org.gethydrated.hydra.core.xml;

import org.gethydrated.hydra.api.service.deploy.ArchiveSpec;
import org.gethydrated.hydra.api.service.deploy.ArchiveSpec.Builder;
import org.gethydrated.hydra.api.service.deploy.ServiceSpec;
import org.gethydrated.hydra.util.xml.XMLParser;
import org.w3c.dom.Element;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class ArchiveParser implements XMLParser<Builder> {

    private ArchiveSpec.Builder archive;

    private boolean complete = false;

    private List<ServiceSpec> services;

    private XMLParser<?> delegate = null;

    @Override
    public Builder getResult() {
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
        archive = ArchiveSpec.build();
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
        for (final ServiceSpec s : services) {
            archive.addService(s);
        }
    }

    private void parseServiceStart(final Element element) {
        delegate = new ServiceParser();
        delegate.startElement(element);
    }

    private void parseServiceEnd(final Element element) {
        delegate.endElement(element);
        final ServiceSpec s = (ServiceSpec) delegate.getResult();
        if (s != null) {
            services.add(s);
        }
        delegate = null;
    }

}
