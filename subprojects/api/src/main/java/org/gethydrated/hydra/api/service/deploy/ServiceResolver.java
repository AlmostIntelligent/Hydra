package org.gethydrated.hydra.api.service.deploy;

/**
 *
 */
public interface ServiceResolver extends AutoCloseable {

    ArchiveSpec resolveArchive(String name);

    ArchiveSpec resolveArchive(String name, String version);

    ServiceSpec resolveService(String archive, String name);

    ServiceSpec resolveService(String archive, String archiveVersion, String name, String version);

}
