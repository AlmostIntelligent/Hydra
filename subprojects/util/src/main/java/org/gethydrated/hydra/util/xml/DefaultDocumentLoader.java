package org.gethydrated.hydra.util.xml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/**
 * Default document loader implementation.
 */
public class DefaultDocumentLoader implements DocumentLoader {

    /**
     * JAXP attribute used to configure the schema language for validation.
     */
    private static final String SCHEMA_LANGUAGE_ATTRIBUTE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    /**
     * JAXP attribute value indicating the XSD schema language.
     */
    private static final String XSD_SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema";

    private final ErrorHandler errorHandler;

    private final EntityResolver entityResolver;

    /**
     * Constructor.
     */
    public DefaultDocumentLoader() {
        this.errorHandler = new DefaultErrorHandler();
        this.entityResolver = new LocalSchemaResolver();
    }

    /**
     * Constructor.
     * @param entityResolver schema resolver.
     * @param errorHandler error handler.
     */
    public DefaultDocumentLoader(final EntityResolver entityResolver,
            final ErrorHandler errorHandler) {
        this.entityResolver = entityResolver;
        this.errorHandler = errorHandler;
    }

    @Override
    public Document loadDocument(final InputStream inputStream)
            throws ParserConfigurationException, IOException, SAXException {
        final DocumentBuilderFactory builderFactory = DocumentBuilderFactory
                .newInstance();
        builderFactory.setValidating(true);
        builderFactory.setNamespaceAware(true);
        builderFactory.setAttribute(SCHEMA_LANGUAGE_ATTRIBUTE,
                XSD_SCHEMA_LANGUAGE);
        final DocumentBuilder builder = builderFactory.newDocumentBuilder();
        builder.setErrorHandler(errorHandler);
        builder.setEntityResolver(entityResolver);
        return builder.parse(inputStream);
    }
}
