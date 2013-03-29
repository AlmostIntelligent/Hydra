package org.gethydrated.hydra.core;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorFactory;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.api.Hydra;
import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.api.configuration.ConfigItemTypeException;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.SIDFactory;
import org.gethydrated.hydra.config.ConfigurationImpl;
import org.gethydrated.hydra.core.cli.CLIService;
import org.gethydrated.hydra.core.concurrent.DistributedLockManager;
import org.gethydrated.hydra.core.internal.Archives;
import org.gethydrated.hydra.core.internal.NodeConnector;
import org.gethydrated.hydra.core.internal.Nodes;
import org.gethydrated.hydra.core.messages.StartService;
import org.gethydrated.hydra.core.messages.StopService;
import org.gethydrated.hydra.core.registry.GlobalRegistry;
import org.gethydrated.hydra.core.registry.LocalRegistry;
import org.gethydrated.hydra.core.service.Services;
import org.gethydrated.hydra.core.sid.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Hydra implementation.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 * 
 */
public final class HydraImpl implements InternalHydra {

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

    private Archives archives;

    private DefaultSIDFactory sidFactory;

    private IdMatcher idMatcher = new IdMatcher();

    /**
     * Constructor.
     * 
     * @param cfg
     *            Hydra configuration.
     */
    public HydraImpl(final ConfigurationImpl cfg) throws HydraException {
        logger.info("Starting Hydra.");
        this.cfg = cfg;
        shutdownhook.register();
        archives = new Archives(cfg);
        createNodeUUID();
        try {
            actorSystem = ActorSystem.create(cfg.getSubItems("actors"));
            sidFactory = new DefaultSIDFactory(actorSystem, idMatcher);
            initSystemActors();
        } catch (ConfigItemNotFoundException|ConfigItemTypeException  e) {
            logger.error("Invalid configuration.", e);
            throw new HydraException(e);
        }
    }

    private void createNodeUUID() {
        UUID localId = UUID.randomUUID();
        idMatcher.setLocal(localId);
        logger.debug("Local node uuid: {}", localId);
    }

    private void initSystemActors() {
        services = actorSystem.spawnActor(new ActorFactory() {
            @Override
            public Actor create() throws Exception {
                return new Services(cfg, archives, sidFactory);
            }
        }, "services");
        actorSystem.spawnActor(new ActorFactory() {
            @Override
            public Actor create() throws Exception {
                return new CLIService(HydraImpl.this);
            }
        }, "cli");
        actorSystem.spawnActor(new ActorFactory() {
            @Override
            public Actor create() throws Exception {
                return new DistributedLockManager(idMatcher);
            }
        }, "locking");
        actorSystem.spawnActor(new ActorFactory() {
            @Override
            public Actor create() throws Exception {
                return new Nodes(idMatcher);
            }
        }, "nodes");
        actorSystem.spawnActor(new ActorFactory() {
            @Override
            public Actor create() throws Exception {
                return new NodeConnector(cfg, idMatcher);
            }
        }, "connector");
        actorSystem.spawnActor(new ActorFactory() {
            @Override
            public Actor create() throws Exception {
                return new LocalRegistry(sidFactory);
            }
        }, "localregistry");
        actorSystem.spawnActor(new ActorFactory() {
            @Override
            public Actor create() throws Exception {
                return new GlobalRegistry(sidFactory, idMatcher);
            }
        }, "globalregistry");
    }

    @Override
    public void shutdown() {
        logger.info("Stopping Hydra.");
        shutdownhook.unregister();
        if(actorSystem != null) {
            actorSystem.shutdown();
        }
    }

    @Override
    public void await() throws InterruptedException {
        logger.info("await");
        actorSystem.await();
    }


    @Override
    public SID startService(final String name) throws HydraException {
        if(actorSystem.isTerminated()) {
            throw new HydraException("Hydra already shut down.");
        }
        Future f = services.ask(new StartService(name));
        try {
            return (SID)f.get();
        } catch (InterruptedException|ExecutionException e) {
            throw new HydraException(e);
        }
    }

    @Override
    public void stopService(final SID id) throws HydraException {
        if(actorSystem.isTerminated()) {
            throw new HydraException("Hydra already shut down.");
        }
        if(id.getUSID().getTypeId() != 0) {
            throw new IllegalArgumentException("Cannot stop system services. Try hydra.shutdown() instead.");
        }
        if (((InternalSID) id).getRef().isTerminated()) {
            return;
        }
        if(id instanceof LocalSID) {
            services.tell(new StopService((InternalSID) id), null);
        }
        if(id instanceof ForeignSID) {
            //TODO: send stopservice to node actor
        }
    }

    @Override
    public ActorSystem getActorSystem() {
        return actorSystem;
    }

    @Override
    public Configuration getConfiguration() {
        return cfg;
    }

    @Override
    public SIDFactory getDefaultSIDFactory() {
        return sidFactory;
    }

    @Override
    public IdMatcher getIdMatcher() {
        return idMatcher;
    }
}
