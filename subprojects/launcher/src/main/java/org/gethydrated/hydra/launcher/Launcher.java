package org.gethydrated.hydra.launcher;

import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;

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
            Module.registerURLStreamHandlerFactoryModule(Module.getBootModuleLoader().loadModule(ModuleIdentifier.create("org.jboss.jboss-vfs")));
            checkWorkingDirectories();
            BootStrapper.bootstrap(args);
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
    private static void checkWorkingDirectories() {
        String home = System.getProperty("hydra.home.dir");
        if (home == null || home.equals("")) {
            throw new IllegalArgumentException("Hydra home directory not set! Use property 'hydra.home.dir'");
        }
        String conf = System.getProperty("hydra.conf.dir");
        if (conf == null || conf.equals("")) {
            conf = home + File.separator + "conf";
            System.setProperty("hydra.conf.dir", conf);
            System.out.println("INFO: hydra.conf.dir not set, defaulting to '" + conf + "'");
        }
        String deploy = System.getProperty("hydra.deploy.dir");
        if (deploy == null || deploy.equals("")) {
            deploy = home + File.separator + "services";
            System.setProperty("hydra.deploy.dir", deploy);
            System.out.println("INFO: hydra.deploy.dir not set, defaulting to '" + deploy + "'");
        }
    }

    /**
     * Prints hydra usage.
     */
    public static void printUsage() {
        System.out.println("Hydra Usage:");
    }
}
