package org.gethydrated.hydra.core.internal;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.core.io.ArchiveLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Archives manager.
 */
public class Archives {

    private final Configuration cfg;

    private final ConcurrentMap<String, Archive> archives = new ConcurrentHashMap<>();

    private final Logger logger = LoggerFactory.getLogger(Archives.class);

    /**
     * Constructor.
     * @param cfg Hydra configuration.
     */
    public Archives(final Configuration cfg) {
        this.cfg = cfg;
        init();
    }

    private void init() {
        final List<Archive> archiveList = new ArchiveLoader(cfg).load();
        for (final Archive a : archiveList) {
            archives.put(a.getName(), a);
        }
    }

    /**
     * Returns a service archive for a given name.
     * @param archiveName archive name.
     * @return service archive.
     */
    public Archive getArchive(final String archiveName) {
        logger.debug("looking up '{}'", archiveName);
        final Archive ar = archives.get(archiveName);
        logger.debug("found: {}", ar);
        return ar;
    }

    /**
     * Returns a service for a given name.
     * @param serviceName service name.
     * @return service.
     */
    public Service getService(final String serviceName) {
        final String[] arr = serviceName.split("::");
        if (arr.length != 2) {
            logger.error(
                    "Servicename didnt match pattern 'archive::service': {}",
                    serviceName);
            return null;
        }
        final Archive archive = getArchive(arr[0]);
        if (archive != null) {
            return archive.getService(arr[1]);
        }
        logger.error("Could not find archive '{}'", arr[0]);
        return null;
    }
}
