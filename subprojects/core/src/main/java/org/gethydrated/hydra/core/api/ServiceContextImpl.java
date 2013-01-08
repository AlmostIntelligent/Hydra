package org.gethydrated.hydra.core.api;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.api.service.MessageHandler;
import org.gethydrated.hydra.api.service.ServiceContext;
import org.gethydrated.hydra.core.configuration.ConfigurationSecurityWrapper;
import org.gethydrated.hydra.core.service.ServiceImpl;

/**
 * Service context api implementation.
 * @author Christian Kulpa
 * @since 0.1.0
 *
 */
public class ServiceContextImpl extends ServiceApiImpl implements
        ServiceContext {
    
    /**
     * Configuration.
     */
    private Configuration cfg;

    private ServiceImpl service;

    /**
     * Constructor.
     * @param cfg Configuration.
     */
    public ServiceContextImpl(final ServiceImpl service, final Configuration cfg) {
        this.cfg = new ConfigurationSecurityWrapper(cfg);
        this.service = service;
    }

    @Override
    public final Configuration getConfiguration() {
        return cfg;
    }

    @Override
    public <T> void registerMessageHandler(Class<T> classifier, MessageHandler<T> messageHandler) {
        service.addMessageHandler(classifier, messageHandler);
    }

    @Override
    public void subscribeEvent(Class<?> classifier) {
        service.getSystem().getEventStream().subscribe(service.getSelf(), classifier);
    }

}

