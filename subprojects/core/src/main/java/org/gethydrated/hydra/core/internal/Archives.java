package org.gethydrated.hydra.core.internal;

import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.core.io.ArchiveLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 */
public class Archives {

    private Configuration cfg;

    private ConcurrentMap<String, Archive> archives = new ConcurrentHashMap<>();

    private final Logger logger = LoggerFactory.getLogger(Archives.class);

    public Archives(Configuration cfg) {
        this.cfg = cfg;
        init();
    }

    private void init() {
        List<Archive> archiveList = new ArchiveLoader(cfg).load();
        for(Archive a: archiveList) {
            archives.put(a.getName(), a);
        }
    }

    public Archive getArchive(String archiveName) {
        logger.debug("looking up '{}'", archiveName);
        Archive ar = archives.get(archiveName);
        logger.debug("found: {}", ar);
        return ar;
    }

    public Service getService(String serviceName) {
        String[] arr = serviceName.split("::");
        if(arr.length != 2) {
            logger.error("Servicename didnt match pattern 'archive::service': {}", serviceName);
            return null;
        }
        Archive archive = getArchive(arr[0]);
        if(archive != null) {
            return archive.getService(arr[1]);
        }
        logger.error("Could not find archive '{}'", arr[0]);
        return null;
    }
}
