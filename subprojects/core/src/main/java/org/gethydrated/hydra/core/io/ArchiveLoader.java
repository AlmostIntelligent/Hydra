package org.gethydrated.hydra.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarFile;

import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.core.internal.Archive;
import org.gethydrated.hydra.core.xml.ArchiveReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Archive loader.
 */
public final class ArchiveLoader {

    private final Path directory;

    private final Logger logger = LoggerFactory.getLogger(ArchiveLoader.class);

    private final ArchiveReader archiveReader = new ArchiveReader();

    /**
     * Constructor.
     * @param cfg hydra configuration.
     */
    public ArchiveLoader(final Configuration cfg) {
        directory = Paths.get(System.getProperty("hydra.home") + "/service");
    }

    /**
     * Loads all archives in the service directory.
     * @return List of all loaded archives.
     */
    public List<Archive> load() {
        logger.debug("Checking available archives in '{}'", directory);

        final List<Archive> archives = new LinkedList<>();

        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFileFailed(final Path file,
                        final IOException exc) {
                    return FileVisitResult.SKIP_SUBTREE;
                }

                @Override
                public FileVisitResult visitFile(final Path path,
                        final BasicFileAttributes attribs) {
                    try {
                        logger.debug("found '{}'", path.toString());
                        if (path.toString().endsWith(".jar")
                                || path.toString().endsWith(".war")
                                || path.toString().endsWith(".ear")) {
                            logger.debug("Checking '{}'", path.toString());
                            try (JarFile jf = new JarFile(path.toFile()); InputStream is = jf.getInputStream(jf
                                    .getEntry("HYDRA-INF/archive.xml"))) {
                                final Archive ar = archiveReader.parse(is);
                                if (ar != null) {
                                    ar.addArchiveURL(path.toUri().toURL());
                                    archives.add(ar);
                                }
                            }

                        }
                    } catch (final Exception e) {
                        logger.error("Error while parsing archive data: {}", e);
                    }
                    return FileVisitResult.CONTINUE;
                }

            });
        } catch (final IOException e) {
            e.printStackTrace();
        }

        logger.debug("Archives found: {}", archives.size());
        return archives;
    }

}
