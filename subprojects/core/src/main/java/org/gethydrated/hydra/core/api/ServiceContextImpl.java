package org.gethydrated.hydra.core.api;

import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.api.service.Service;
import org.gethydrated.hydra.api.service.ServiceContext;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.core.configuration.ConfigurationSecurityWrapper;
import org.gethydrated.hydra.core.service.ServiceManager;

/**
 * Service context api implementation.
 * @author Christian Kulpa
 * @since 0.1.0
 *
 */
public class ServiceContextImpl extends ServiceApiImpl implements
        ServiceContext {

    /**
     * Service.
     */
    private Service service;
    
    /**
     * Configuration.
     */
    private Configuration cfg;
    
    /**
     * Constructor.
     * @param sm ServiceManager.
     * @param s Service.
     * @param cfg Configuration.
     */
    public ServiceContextImpl(final ServiceManager sm, final Service s, final Configuration cfg) {
        super(sm);
        service = s;
        this.cfg = cfg;
    }

    @Override
    public final Service getService() {
        return service;
    }

    @Override
    public final Configuration getConfiguration() {
        return new ConfigurationSecurityWrapper(cfg);
    }

}

