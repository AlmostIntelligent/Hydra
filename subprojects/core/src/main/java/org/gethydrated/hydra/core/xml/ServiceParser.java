package org.gethydrated.hydra.core.xml;

import org.gethydrated.hydra.core.internal.Dependency;
import org.gethydrated.hydra.core.internal.Service;
import org.gethydrated.hydra.util.xml.XMLParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class ServiceParser implements XMLParser<Service> {

    private final Logger logger = LoggerFactory.getLogger(ServiceParser.class);

    private Service service;

    private boolean complete = false;

    private XMLParser<?> delegate;

    private List<Dependency> deps;

    @Override
    public Service getResult() {
        return complete ? service : null;
    }

    @Override
    public void startElement(Element element) {
        switch (element.getTagName()) {
            case "service":     parseServiceStart(element);
                break;
            case "name":        parseName(element);
                break;
            case "version":     parseVersion(element);
                break;
            case "activator":   parseActivator(element);
            case "dependencies":parseDepencendiesStart(element);
                break;
            case "dependency":  parseDependency(element);
                break;
            case "configuration":   parseConfig(element);
            default:
                if(delegate!=null) {
                    delegate.startElement(element);
                }
        }
    }

    @Override
    public void endElement(Element element) {
        switch (element.getTagName()) {
            case "service":     parseServiceEnd(element);
                break;
            case "dependencies":parseDependenciesEnd(element);
            default:
                if(delegate!=null) {
                    delegate.endElement(element);
                }
        }
    }

    private void parseServiceStart(Element element) {
        service = new Service();
    }

    private void parseServiceEnd(Element element) {
        complete = true;
    }

    private void parseName(Element element) {
        logger.info("Setting service name: {}", element.getTextContent());
        service.setName(element.getTextContent());
    }

    private void parseVersion(Element element) {
        service.setVersion(element.getTextContent());
    }

    private void parseActivator(Element element) {
        service.setActivator(element.getTextContent());
    }

    private void parseDepencendiesStart(Element element) {
        deps = new LinkedList<>();
    }

    private void parseDependenciesEnd(Element element) {
        for(Dependency dep : deps) {
            service.addDependency(dep);
        }
    }

    private void parseDependency(Element element) {
        String name = element.getAttribute("name");
        String version = element.getAttribute("version");
        if(name != null) {
            deps.add(new Dependency(name, version));
        }
    }

    private void parseConfig(Element element) {
        logger.warn("Configuration parsing not implemented yet");
    }
}
