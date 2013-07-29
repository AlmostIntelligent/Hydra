package org.gethydrated.hydra.core.xml;

import org.gethydrated.hydra.api.service.deploy.ArchiveSpec.Builder;
import org.gethydrated.hydra.util.xml.AbstractXMLReader;
import org.gethydrated.hydra.util.xml.XMLParser;

/**
 *
 */
public class ArchiveReader extends AbstractXMLReader<Builder> {

    public ArchiveReader() {
        super(ArchiveReader.class.getClassLoader());
    }

    @Override
    protected XMLParser<Builder> getParser() {
        return new ArchiveParser();
    }
}