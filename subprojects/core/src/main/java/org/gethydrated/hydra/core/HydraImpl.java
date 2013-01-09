package org.gethydrated.hydra.core;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorFactory;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.api.Hydra;
import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.api.configuration.ConfigItemTypeException;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.config.ConfigurationImpl;
import org.gethydrated.hydra.core.messages.StartService;
import org.gethydrated.hydra.core.service.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
    private final Logger logger = LoggerFactory.getLogger(Hydra.class);

    /**
     * Shutdown hook.
     */
    private final ShutdownHook shutdownhook = new ShutdownHook(this);

    /**
     * Hydra configuration.
     */
    private final ConfigurationImpl cfg;
    
    private ActorSystem actorSystem;

    private ActorRef services;

    /**
     * Constructor.
     * 
     * @param cfg
     *            Hydra configuration.
     */
    public HydraImpl(final ConfigurationImpl cfg) {
        this.cfg = cfg;

    }

    @Override
    public synchronized void start() throws HydraException {
        if(actorSystem==null) {
            logger.info("Starting Hydra.");
            try {
                System.out.println(cfg.list());
                actorSystem = ActorSystem.create(cfg.getSubItems("actors"));
                services = actorSystem.spawnActor(new ActorFactory() {
                    @Override
                    public Actor create() throws Exception {
                        return new Services(cfg);
                    }
                }, "services");
                shutdownhook.register();
            } catch (ConfigItemNotFoundException|ConfigItemTypeException  e) {
                logger.error("Invalid configuration.", e);
                throw new HydraException(e);
            }
        }
    }

    @Override
    public synchronized void stop() {
        if(actorSystem!=null) {
            logger.info("Stopping Hydra.");
            shutdownhook.unregister();
            actorSystem.shutdown();
            actorSystem = null;
        }
    }

    @Override
    public synchronized SID startService(final String name) throws HydraException {
        if(actorSystem==null) {
            throw new IllegalStateException("Hydra not running.");
        }
        Future f = services.ask(new StartService(name));
        try {
            SID i =  (SID)f.get();
            return i;
        } catch (InterruptedException|ExecutionException e) {
            throw new HydraException(e);
        }
    }

    @Override
    public synchronized void stopService(final SID id) throws HydraException {
        if(actorSystem==null) {
            throw new IllegalStateException("Hydra not running.");
        }
    }

}
