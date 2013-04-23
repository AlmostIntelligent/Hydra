package org.gethydrated.hydra.core;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorFactory;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.api.Hydra;
import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.USID;
import org.gethydrated.hydra.config.ConfigurationImpl;
import org.gethydrated.hydra.core.cli.CLIService;
import org.gethydrated.hydra.core.concurrent.DistributedLockManager;
import org.gethydrated.hydra.core.internal.Archives;
import org.gethydrated.hydra.core.internal.Nodes;
import org.gethydrated.hydra.core.io.network.NetKernel;
import org.gethydrated.hydra.core.io.network.NetKernelImpl;
import org.gethydrated.hydra.core.registry.GlobalRegistry;
import org.gethydrated.hydra.core.registry.LocalRegistry;
import org.gethydrated.hydra.core.service.Services;
import org.gethydrated.hydra.core.service.StartService;
import org.gethydrated.hydra.core.service.StopService;
import org.gethydrated.hydra.core.sid.DefaultSIDFactory;
import org.gethydrated.hydra.core.sid.ForeignSID;
import org.gethydrated.hydra.core.sid.InternalSID;
import org.gethydrated.hydra.core.sid.LocalSID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private final Archives archives;

    private DefaultSIDFactory sidFactory;

    private NetKernel netKernel;

    /**
     * Constructor.
     * 
     * @param cfg
     *            Hydra configuration.
     * @throws HydraException on failure.
     */
    public HydraImpl(final ConfigurationImpl cfg) throws HydraException {
        logger.info("Starting Hydra.");
        this.cfg = cfg;
        shutdownhook.register();
        archives = new Archives(cfg);
        try {
            actorSystem = ActorSystem.create(cfg.getSubItems("actors"));
            netKernel = new NetKernelImpl(this);
            sidFactory = new DefaultSIDFactory(actorSystem, netKernel);
            initSystemActors();
        } catch (final Exception e) {
            logger.error("Invalid configuration.", e);
            throw new HydraException(e);
        }
    }

    private void initSystemActors() {
        final InternalHydra hydra = this;
        services = actorSystem.spawnActor(new ActorFactory() {
            @Override
            public Actor create() throws Exception {
                return new Services(archives, hydra);
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
                return new DistributedLockManager(netKernel);
            }
        }, "locking");
        actorSystem.spawnActor(new ActorFactory() {
            @Override
            public Actor create() throws Exception {
                return new Nodes(netKernel, sidFactory);
            }
        }, "nodes");
        actorSystem.spawnActor(new ActorFactory() {
            @Override
            public Actor create() throws Exception {
                return new LocalRegistry(sidFactory);
            }
        }, "localregistry");
        actorSystem.spawnActor(new ActorFactory() {
            @Override
            public Actor create() throws Exception {
                return new GlobalRegistry(hydra);
            }
        }, "globalregistry");
    }

    @Override
    public void shutdown() {
        logger.info("Stopping Hydra.");
        shutdownhook.unregister();
        if (actorSystem != null) {
            actorSystem.shutdown();
        }
        netKernel.close();
    }

    @Override
    public void await() throws InterruptedException {
        logger.info("await");
        actorSystem.await();
    }

    @Override
    public SID startService(final String name) throws HydraException {
        if (actorSystem.isTerminated()) {
            throw new HydraException("Hydra already shut down.");
        }
        final Future<?> f = services.ask(new StartService(name));
        try {
            return (SID) f.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new HydraException(e);
        }
    }

    @Override
    public SID getService(final String name) {
        return sidFactory.fromString(name);
    }

    @Override
    public SID getService(final USID usid) {
        return sidFactory.fromUSID(usid);
    }

    @Override
    public void stopService(final SID id) throws HydraException {
        if (actorSystem.isTerminated()) {
            throw new HydraException("Hydra already shut down.");
        }
        if (id == null) {
            return;
        }
        if (id.getUSID().getTypeId() != 0) {
            throw new IllegalArgumentException(
                    "Cannot stop system services. Try hydra.shutdown() instead.");
        }
        if (((InternalSID) id).getRef().isTerminated()) {
            return;
        }
        if (id instanceof LocalSID) {
            services.tell(new StopService(id.getUSID()), null);
        }
        if (id instanceof ForeignSID) {
            final ActorRef ref = actorSystem.getActor("/app/nodes/"
                    + netKernel.getID(id.getUSID().getNodeId()));
            ref.tell(new StopService(id.getUSID()), null);
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
    public DefaultSIDFactory getDefaultSIDFactory() {
        return sidFactory;
    }

    @Override
    public NetKernel getNetKernel() {
        return netKernel;
    }
}
