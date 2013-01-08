package org.gethydrated.hydra.core;

import org.gethydrated.hydra.api.Hydra;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JVM shutdown hook. Stops Hydra on jvm shutdown.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 *
 */
public class ShutdownHook implements Runnable {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory
            .getLogger(ShutdownHook.class);

    /**
     * Thread reference.
     */
    private final Thread hook = new Thread(this);

    /**
     * Hydra instance.
     */
    private final Hydra hydra;

    /**
     * Constructor.
     * @param h Hydra instance.
     */
    public ShutdownHook(final Hydra h) {
        hydra = h;
    }

    @Override
    public final void run() {
        LOG.info("JVM Shutdown detected.");
        hydra.stop();
    }

    /**
     * Registers the hook.
     */
    public final void register() {
        LOG.debug("Registering shutdown hook.");
        Runtime.getRuntime().addShutdownHook(hook);
    }

    /**
     * Unregisters the hook.
     * @return true when successfully unregistered.
     */
    public final boolean unregister() {
        LOG.debug("Unregistering shutdown hook.");
        try {
            return Runtime.getRuntime().removeShutdownHook(hook);
        } catch (IllegalStateException e) {
            return false;
        }
    }

}
