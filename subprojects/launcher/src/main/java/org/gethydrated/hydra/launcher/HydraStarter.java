package org.gethydrated.hydra.launcher;

import org.gethydrated.hydra.api.Hydra;
import org.gethydrated.hydra.core.HydraFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Starts Hydra.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 * 
 */
public final class HydraStarter {

    /**
     * Hide constructor to prevent instanciation.
     */
    private HydraStarter() {
    }

    /**
     * Starts Hydra framework.
     * 
     * @param args
     *            arguments
     * @throws Exception
     *             on failure.
     */
    public static void start(final String[] args) throws Exception {
        configureLogback(System.getProperty("hydra.home"));
        final Logger logger = LoggerFactory.getLogger(HydraStarter.class);
        try {
            final Hydra hydra = HydraFactory.getHydra();
            hydra.await();
        } catch (final Exception e) {
            logger.error("An error occured while running hydra:", e);
        }
    }

    /**
     * Configures Logback.
     * 
     * @param hydraHome
     *            hydra home directory.
     * @throws Exception
     *             on failure.
     */
    private static void configureLogback(final String hydraHome)
            throws Exception {
        final Map<String, String> properties = new HashMap<>();
        properties.put("HYDRA_HOME", hydraHome);
        final URL file = new File(System.getProperty("hydra.conf.dir")+"/logging.xml").toURI().toURL();
        if (file != null) {
            LogbackConfigurator.configure(file, properties);
        } else {
            throw new IllegalStateException("Could not find logback.xml file");
        }
    }
}
