package org.gethydrated.hydra.util.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 */
public class DefaultErrorHandler implements ErrorHandler {

    private final Logger logger = LoggerFactory.getLogger(DefaultErrorHandler.class);

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        logger.warn("{}", exception.getLocalizedMessage());
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        logger.error("{}", exception.getLocalizedMessage());
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        logger.error("{}", exception.getLocalizedMessage());
    }
}
