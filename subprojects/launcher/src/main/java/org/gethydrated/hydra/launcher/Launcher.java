package org.gethydrated.hydra.launcher;

import java.io.File;
import java.net.URISyntaxException;

public class Launcher {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        File hydraHome = getWorkingDirectory();
        System.setProperty("hydra.home", hydraHome.getPath());
        BootStrapper.bootstrap(args, hydraHome);
    }

    private static File getWorkingDirectory() {
        try {
            File path = new File(Launcher.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI());
            return path.getParentFile().getParentFile();
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Could not detect Hydra directory",
                    e);
        }
    }

    public static void printUsage() {
        System.out.println("Hydra Usage:");
    }
}
