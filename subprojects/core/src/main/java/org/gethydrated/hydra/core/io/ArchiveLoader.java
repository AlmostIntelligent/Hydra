package org.gethydrated.hydra.core.io;

import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.core.xml.ArchiveReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public final class ArchiveLoader {

    private final Path directory;

    private final Logger logger = LoggerFactory.getLogger(ArchiveLoader.class);

    private final ArchiveReader archiveReader = new ArchiveReader();

    public ArchiveLoader(final Configuration cfg) {
        directory = Paths.get(System.getProperty("hydra.home") + "/service");
    }

    public List<Archive> load() {
        logger.debug("Checking available archives in '{}'", directory);

        final List<Archive> archives = new LinkedList<>();

        try {
            Files.walkFileTree(directory,new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFileFailed( Path file, IOException exc )
                {
                    return FileVisitResult.SKIP_SUBTREE;
                }

                @Override
                public FileVisitResult visitFile( Path path, BasicFileAttributes attribs )
                {
                    try {
                    logger.debug("found '{}'", path.toString());
                    if(path.toString().endsWith(".jar") || path.toString().endsWith(".war") || path.toString().endsWith(".ear")) {
                        logger.debug("Checking '{}'", path.toString());
                        Archive ar = archiveReader.parse(new FileInputStream(path.toFile()));
                        if(ar != null) {
                            archives.add(ar);
                        }
                    }
                    } catch (IOException e) {
                        logger.error("Error while parsing archive data: {}", e);
                    }
                    return FileVisitResult.CONTINUE;
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.debug("Archives found: {}", archives.size());
        return archives;
    }

}
