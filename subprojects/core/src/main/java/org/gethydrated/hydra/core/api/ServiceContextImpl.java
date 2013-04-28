package org.gethydrated.hydra.core.api;

import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.api.service.MessageHandler;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.ServiceContext;
import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.configuration.ConfigurationSecurityWrapper;
import org.gethydrated.hydra.core.service.ServiceImpl;
import org.gethydrated.hydra.core.sid.DefaultSIDFactory;

/**
 * Service context api implementation.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 * 
 */
public class ServiceContextImpl extends ServiceApiImpl implements
        ServiceContext {

    /**
     * Configuration.
     */
    private final Configuration cfg;

    private final ServiceImpl service;

    private final DefaultSIDFactory sidFactory;

    /**
     * Constructor.
     * 
     * @param service service implementation.
     * @param hydra Internal Hydra representation.
     */
    public ServiceContextImpl(final ServiceImpl service,
            final InternalHydra hydra) {
        super(hydra);
        this.sidFactory = (DefaultSIDFactory) hydra.getDefaultSIDFactory();
        this.cfg = new ConfigurationSecurityWrapper(hydra.getConfiguration());
        this.service = service;
    }

    @Override
    public SID getSelf() {
        return sidFactory.fromActorRef(service.getSelf());
    }

    @Override
    public SID getOutput() {
        return sidFactory.fromActorRef(service.getContext()
                .getActor("/sys/out"));
    }

    @Override
    public final Configuration getConfiguration() {
        return cfg;
    }

    @Override
    public <T> void registerMessageHandler(final Class<T> classifier,
            final MessageHandler<T> messageHandler) {
        service.addMessageHandler(classifier, messageHandler);
    }

    @Override
    public void subscribeEvent(final Class<?> classifier) {
        service.getSystem().getEventStream()
                .subscribe(service.getSelf(), classifier);
    }
}
