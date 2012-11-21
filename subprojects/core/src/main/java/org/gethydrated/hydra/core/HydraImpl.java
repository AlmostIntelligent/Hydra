package org.gethydrated.hydra.core;

import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.api.Hydra;
import org.gethydrated.hydra.api.HydraApi;
import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.core.api.HydraApiImpl;
import org.gethydrated.hydra.core.configuration.ConfigurationImpl;
import org.gethydrated.hydra.core.message.MessageDispatcher;
import org.gethydrated.hydra.core.message.MessageQueue;
import org.gethydrated.hydra.core.service.ServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hydra implementation.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 * 
 */
public final class HydraImpl implements Hydra {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(Hydra.class);

    /**
     * Shutdown hook.
     */
    private final ShutdownHook shutdownhook = new ShutdownHook(this);

    /**
     * Hydra configuration.
     */
    private final ConfigurationImpl cfg;
    
    private final ActorSystem actorSystem;

    /**
     * Constructor.
     * 
     * @param cfg
     *            Hydra configuration.
     */
    public HydraImpl(final ConfigurationImpl cfg) {
        this.cfg = cfg;
        actorSystem = ActorSystem.create();
    }

    @Override
    public void start() {
        LOG.info("Starting Hydra.");
        shutdownhook.register();
    }

    @Override
    public void stop() {
        LOG.info("Stopping Hydra.");
        shutdownhook.unregister();
        actorSystem.shutdown();
    }

    @Override
    public Long startService(final String name) throws HydraException {
        return null;
    }

    @Override
    public void stopService(final Long id) throws HydraException {
        
    }

}
