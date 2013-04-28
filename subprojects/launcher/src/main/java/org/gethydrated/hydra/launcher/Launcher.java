package org.gethydrated.hydra.launcher;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Simple Hydra launch application.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 * 
 */
public final class Launcher {

    /**
     * Hide constructor to prevent instanciation.
     */
    private Launcher() {
    }

    /**
     * @param args
     *            arguments
     * @throws Exception
     *             on failure.
     */
    public static void main(final String[] args) throws Exception {
        final File hydraHome = getWorkingDirectory();
        System.setProperty("hydra.home", hydraHome.getPath());
        BootStrapper.bootstrap(args, hydraHome);
    }

    /**
     * Detects Hydra home directory.
     * 
     * @return Hydra home directory.
     */
    private static File getWorkingDirectory() {
        try {
            final File path = new File(Launcher.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI());
            return path.getParentFile().getParentFile();
        } catch (final URISyntaxException e) {
            throw new IllegalStateException("Could not detect Hydra directory",
                    e);
        }
    }

    /**
     * Prints hydra usage.
     */
    public static void printUsage() {
        System.out.println("Hydra Usage:");
    }
}
