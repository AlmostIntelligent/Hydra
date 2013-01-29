package org.gethydrated.hydra.core.service;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorFactory;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.api.util.IDGenerator;
import org.gethydrated.hydra.core.internal.Archives;
import org.gethydrated.hydra.core.internal.Service;
import org.gethydrated.hydra.core.messages.StartService;
import org.gethydrated.hydra.core.service.locator.ServiceLocator;
import org.gethydrated.hydra.core.service.locator.SystemServiceLocator;
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

    /**
     * Service locator.
     */
    private final ServiceLocator sl;

    private final IDGenerator idGen;

    /**
     * Configuration.
     */
    private final Configuration cfg;

    private final Archives archives;
    
    /**
     * Constructor.
     * 
     * @param cfg
     *            Configuration.
     */
    public Services(final Configuration cfg, final Archives archives) {
        sl = new SystemServiceLocator(cfg);
        this.archives = archives;
        idGen = new IDGenerator();
        this.cfg = cfg;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof StartService) {
            startService(((StartService) message).getServiceName());
        }
    }

    private void startService(String serviceName) {
        try {
            Long id = idGen.getId();
            final Service service = archives.getService(serviceName);
            if(service != null) {
                ActorRef ref = getContext().spawnActor(new ActorFactory() {
                    @Override
                    public Actor create() throws Exception {
                        return new ServiceImpl(service.getActivator(), service.getClassLoader(), cfg);
                    }
                }, id.toString());
                getSender().tell(new SIDImpl(ref), getSelf());
            }
            throw new RuntimeException("Service not found:"+serviceName);
            /*final ServiceInfo si = sl.locate(serviceName);
            if(si != null) {
                ActorRef service = getHydra().spawnActor(new ActorFactory() {
                    @Override
                    public Actor create() throws Exception {
                        return new ServiceImpl(si, cfg);
                    }
                }, id.toString());
                getSender().tell(new SIDImpl(service), getSelf());
            }  */
        } catch (Throwable e) {
            getSender().tell(e, getSelf());
        }
    }

    @Override
    public void onStart() {
        logger.info("Starting service managing.");
    }
}
