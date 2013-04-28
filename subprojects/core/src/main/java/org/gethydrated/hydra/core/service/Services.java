package org.gethydrated.hydra.core.service;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorFactory;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.service.USID;
import org.gethydrated.hydra.api.util.IDGenerator;
import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.internal.Archives;
import org.gethydrated.hydra.core.internal.Service;
import org.gethydrated.hydra.core.sid.DefaultSIDFactory;
import org.slf4j.Logger;

/**
 * Manages running services.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 * 
 */
public class Services extends Actor {
    /**
     * Logger.
     */
    private final Logger logger = getLogger(Services.class);

    private final IDGenerator idGen;

    private final Archives archives;

    private final DefaultSIDFactory sidFactory;
    private final InternalHydra hydra;

    /**
     * Constructor.
     * @param archives service archives.
     * @param hydra internal Hydra representation.
     */
    public Services(final Archives archives,
            final InternalHydra hydra) {
        this.archives = archives;
        this.sidFactory = (DefaultSIDFactory) hydra.getDefaultSIDFactory();
        this.hydra = hydra;
        idGen = new IDGenerator();
    }

    @Override
    public void onReceive(final Object message) throws Exception {
        if (message instanceof StartService) {
            startService(((StartService) message).getName());
        }
        if (message instanceof StopService) {
            stopService(((StopService) message).getUsid());
        }
    }

    private void stopService(final USID usid) {
        final ActorRef ref = getSystem().getActor(
                DefaultSIDFactory.usidToActorPath(usid));
        getContext().stopActor(ref);
    }

    private void startService(final String serviceName) {
        try {
            final Service service = archives.getService(serviceName);
            if (service != null) {
                final Long id = idGen.getId();
                final ActorRef ref = getContext().spawnActor(
                        new ActorFactory() {
                            @Override
                            public Actor create() throws Exception {
                                return new ServiceImpl(service.getActivator(),
                                        service.getClassLoader(), hydra);
                            }
                        }, id.toString());
                getSender().tell(sidFactory.fromActorRef(ref), getSelf());
            } else {
                throw new RuntimeException("Service not found:" + serviceName);
            }
        } catch (final Throwable e) {
            getSender().tell(e, getSelf());
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        logger.info("Starting service managing.");
    }
}
