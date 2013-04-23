package org.gethydrated.hydra.config.files;

import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.util.xml.AbstractXMLReader;
import org.gethydrated.hydra.util.xml.XMLParser;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class XMLConfigurationReader extends AbstractXMLReader<Configuration> {

    @Override
    protected XMLParser<Configuration> getParser() {
        return new ConfigurationParser();
    }
}
