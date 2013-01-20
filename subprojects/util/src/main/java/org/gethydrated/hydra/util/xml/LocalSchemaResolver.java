package org.gethydrated.hydra.util.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class LocalSchemaResolver implements EntityResolver {
    private static final String DEFAULT_SCHEMA_MAPPING = "/schema.conf";

    private final Logger logger = LoggerFactory.getLogger(LocalSchemaResolver.class);

    private final Map<String, String> mappings;

    public LocalSchemaResolver() {
        mappings = readSchemaMappingFile();
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if(systemId != null) {
            URL url = this.getClass().getResource("/"+mappings.get(systemId));
            logger.debug("found mapping: systemId - {}", url.getFile());
            return new InputSource(url.getFile());
        }
        return null;
    }

    private Map<String, String> readSchemaMappingFile() {
        Map<String, String> map = new HashMap<>();
        URL url = this.getClass().getResource(DEFAULT_SCHEMA_MAPPING);
        if(url != null) {
            try (FileReader fileReader = new FileReader(url.getFile())) {
                BufferedReader input = new BufferedReader(fileReader);
                String s;
                while((s = input.readLine()) != null) {
                    String[] arr = s.split("=");
                    if(arr.length == 2) {
                        map.put(arr[0], arr[1]);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            logger.debug("no schema mappings found");
        }

        return map;
    }
}
