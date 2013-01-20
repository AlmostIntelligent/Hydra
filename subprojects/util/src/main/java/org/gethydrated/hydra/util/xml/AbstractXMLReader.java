package org.gethydrated.hydra.util.xml;

import org.w3c.dom.Document;

import java.io.InputStream;

/**
 *
 */
public abstract class AbstractXMLReader<T> implements XMLReader<T> {

    private final DocumentLoader docLoader = new DefaultDocumentLoader();

    @Override
    public T parse(InputStream inputStream) {
        try {
            Document doc = docLoader.loadDocument(inputStream);
            DocumentRunner runner = new DocumentRunner(doc);
            XMLParser<T> parser = getParser();
            runner.traverse(parser);
            return parser.getResult();
        } catch (Exception e) {
            e.printStackTrace();
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
