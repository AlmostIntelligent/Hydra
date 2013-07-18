package org.gethydrated.hydra.launcher;

import java.io.File;

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
        try {
            final File hydraHome = getWorkingDirectory();
            System.setProperty("hydra.home", hydraHome.getPath());
            BootStrapper.bootstrap(args, hydraHome);
        } catch (Throwable t) {
            t.printStackTrace(System.err);
            System.exit(-1);
        }
    }

    /**
     * Detects Hydra home directory.
     * 
     * @return Hydra home directory.
     */
    private static File getWorkingDirectory() {
        String s = System.getProperty("hydra.home.dir");
        return new File(s);
    }

    /**
     * Prints hydra usage.
     */
    public static void printUsage() {
        System.out.println("Hydra Usage:");
    }
}
