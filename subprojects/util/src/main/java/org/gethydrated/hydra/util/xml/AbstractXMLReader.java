package org.gethydrated.hydra.util.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import java.io.InputStream;

/**
 *
 */
public abstract class AbstractXMLReader<T> implements XMLReader<T> {

    private final DocumentLoader docLoader = new DefaultDocumentLoader();

    private final Logger logger = LoggerFactory.getLogger(AbstractXMLReader.class);

    @Override
    public T parse(InputStream inputStream) {
        try {
            Document doc = docLoader.loadDocument(inputStream);
            DocumentRunner runner = new DocumentRunner(doc);
            XMLParser<T> parser = getParser();
            runner.traverse(parser);
            return parser.getResult();
        } catch (Exception e) {
            logger.error("An error occured while parsing: {}", e.getMessage());
        }
        return null;
    }

    /**
     * As XMLParser implementations can hold state,
     * each method invocation must return a new
     * XMLParser instance.
     * @return new XMLParser instance
     */
    protected abstract XMLParser<T> getParser();

}
