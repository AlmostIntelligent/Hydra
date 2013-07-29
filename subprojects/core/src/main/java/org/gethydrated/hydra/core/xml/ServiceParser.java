package org.gethydrated.hydra.core.xml;

import org.gethydrated.hydra.api.service.deploy.ServiceSpec;
import org.gethydrated.hydra.api.service.deploy.ServiceSpec.Builder;
import org.gethydrated.hydra.core.internal.Dependency;
import org.gethydrated.hydra.util.xml.XMLParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import java.util.LinkedList;
import java.util.List;

/**
 * Service parser.
 * 
 * @author Christian Kulpa
 * @author Hanno Sternberg
 * @since 0.2.0
 */
public class ServiceParser implements XMLParser<ServiceSpec> {

    private final Logger logger = LoggerFactory.getLogger(ServiceParser.class);

    private Builder service;

    private boolean complete = false;

    private XMLParser<?> delegate;

    private List<Dependency> deps;

    @Override
    public ServiceSpec getResult() {
        return complete ? service.create() : null;
    }

    @Override
    public void startElement(final Element element) {
        switch (element.getTagName()) {
        case "service":
            parseServiceStart(element);
            break;
        case "name":
            parseName(element);
            break;
        case "version":
            parseVersion(element);
            break;
        case "activator":
            parseActivator(element);
        case "dependencies":
            parseDepencendiesStart(element);
            break;
        case "dependency":
            parseDependency(element);
            break;
        case "configuration":
            parseConfig(element);
        default:
            if (delegate != null) {
                delegate.startElement(element);
            }
        }
    }

    @Override
    public void endElement(final Element element) {
        switch (element.getTagName()) {
        case "service":
            parseServiceEnd(element);
            break;
        case "dependencies":
            parseDependenciesEnd(element);
        default:
            if (delegate != null) {
                delegate.endElement(element);
            }
        }
    }

    private void parseServiceStart(final Element element) {
        service = ServiceSpec.build();
    }

    private void parseServiceEnd(final Element element) {
        complete = true;
    }

    private void parseName(final Element element) {
        logger.info("Setting service name: {}", element.getTextContent());
        service.setName(element.getTextContent());
    }

    private void parseVersion(final Element element) {
        service.setVersion(element.getTextContent());
    }

    private void parseActivator(final Element element) {
        service.setActivator(element.getTextContent());
    }

    private void parseDepencendiesStart(final Element element) {
        deps = new LinkedList<>();
    }

    private void parseDependenciesEnd(final Element element) {
        for (final Dependency dep : deps) {
            //service.addDependency(dep);
        }
    }

    private void parseDependency(final Element element) {
        final String name = element.getAttribute("name");
        final String version = element.getAttribute("version");
        if (name != null) {
            deps.add(new Dependency(name, version));
        }
    }

    private void parseConfig(final Element element) {
        logger.warn("Configuration parsing not implemented yet");
    }
}
