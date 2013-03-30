package org.gethydrated.hydra.core.service;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorFactory;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.api.util.IDGenerator;
import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.internal.Archives;
import org.gethydrated.hydra.core.internal.Service;
import org.gethydrated.hydra.core.messages.StartService;
import org.gethydrated.hydra.core.messages.StopService;
import org.gethydrated.hydra.core.sid.DefaultSIDFactory;
import org.gethydrated.hydra.core.sid.InternalSID;
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

    /**
     * Configuration.
     */
    private final Configuration cfg;

    private final Archives archives;

    private final DefaultSIDFactory sidFactory;
    private InternalHydra hydra;

    /**
     * Constructor.
     *
     * @param cfg
     *            Configuration.
     * @param hydra
     */
    public Services(final Configuration cfg, final Archives archives, InternalHydra hydra) {
        this.archives = archives;
        this.sidFactory = (DefaultSIDFactory) hydra.getDefaultSIDFactory();
        this.hydra = hydra;
        idGen = new IDGenerator();
        this.cfg = cfg;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof StartService) {
            startService(((StartService) message).name);
        }
        if(message instanceof StopService) {
            stopService(((StopService) message).sid);
        }
    }

    private void stopService(InternalSID sid) {
        getContext().stopActor(sid.getRef());
    }

    private void startService(String serviceName) {
        System.out.println(serviceName);
        try {
            final Service service = archives.getService(serviceName);
            System.out.println(service);
            if(service != null) {
                Long id = idGen.getId();
                System.out.println(id);
                ActorRef ref = getContext().spawnActor(new ActorFactory() {
                    @Override
                    public Actor create() throws Exception {
                        return new ServiceImpl(service.getActivator(), service.getClassLoader(), hydra);
                    }
                }, id.toString());
                System.out.println(ref);
                getSender().tell(sidFactory.fromActorRef(ref), getSelf());
            } else {
                throw new RuntimeException("Service not found:"+serviceName);
            }
        } catch (Throwable e) {
            getSender().tell(e, getSelf());
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        logger.info("Starting service managing.");
    }
}
