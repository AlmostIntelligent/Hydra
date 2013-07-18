package org.gethydrated.hydra.core.xml;

import org.gethydrated.hydra.core.internal.Archive;
import org.gethydrated.hydra.util.xml.AbstractXMLReader;
import org.gethydrated.hydra.util.xml.XMLParser;

/**
 *
 */
public class ArchiveReader extends AbstractXMLReader<Archive> {

    public ArchiveReader() {
        super(ArchiveReader.class.getClassLoader());
    }

    @Override
    protected XMLParser<Archive> getParser() {
        return new ArchiveParser();
    }
}