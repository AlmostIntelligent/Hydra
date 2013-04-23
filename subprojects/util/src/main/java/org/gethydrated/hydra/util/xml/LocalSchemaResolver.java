package org.gethydrated.hydra.util.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * XML schema resolver. 
 */
public class LocalSchemaResolver implements EntityResolver {
    private static final String DEFAULT_SCHEMA_MAPPING = "/schema.conf";

    private final Logger logger = LoggerFactory
            .getLogger(LocalSchemaResolver.class);

    private final Map<String, String> mappings;

    /**
     * Constructor.
     */
    public LocalSchemaResolver() {
        mappings = readSchemaMappingFile();
    }

    @Override
    public InputSource resolveEntity(final String publicId,
            final String systemId) throws SAXException, IOException {
        if (systemId != null) {
            final InputStream inputStream = this.getClass()
                    .getResourceAsStream("/" + mappings.get(systemId));
            if (inputStream != null) {
                logger.debug("found mapping: {} - {}", systemId,
                        mappings.get(systemId));
                return new InputSource(inputStream);
            }
        }
        return null;
    }

    private Map<String, String> readSchemaMappingFile() {
        final Map<String, String> map = new HashMap<>();
        try (InputStream inputStream = this.getClass().getResourceAsStream(
                DEFAULT_SCHEMA_MAPPING)) {
            final BufferedReader input = new BufferedReader(
                    new InputStreamReader(inputStream));
            String s;
            while ((s = input.readLine()) != null) {
                final String[] arr = s.split("=");
                if (arr.length == 2) {
                    map.put(arr[0], arr[1]);
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        logger.debug("found {} mappings: {}", map.size(), map);
        return map;
    }
}
