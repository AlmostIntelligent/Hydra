package org.gethydrated.hydra.util.xml;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

/**
 * Base XMLReader class.
 * 
 * @author Chris
 *
 * @param <T> Target type.
 */
public abstract class AbstractXMLReader<T> implements XMLReader<T> {

    private final DocumentLoader docLoader = new DefaultDocumentLoader();

    private final Logger logger = LoggerFactory
            .getLogger(AbstractXMLReader.class);

    @Override
    public final T parse(final InputStream inputStream) {
        try {
            final Document doc = docLoader.loadDocument(inputStream);
            final DocumentRunner runner = new DocumentRunner(doc);
            final XMLParser<T> parser = getParser();
            runner.traverse(parser);
            return parser.getResult();
        } catch (final Exception e) {
            logger.error("An error occured while parsing: {}", e.getMessage());
        }
        return null;
    }

    /**
     * As XMLParser implementations can hold state, each method invocation must
     * return a new XMLParser instance.
     * 
     * @return new XMLParser instance
     */
    protected abstract XMLParser<T> getParser();

}
