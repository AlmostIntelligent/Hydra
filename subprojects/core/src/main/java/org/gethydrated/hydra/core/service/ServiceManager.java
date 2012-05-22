package org.gethydrated.hydra.core.service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.service.Service;
import org.gethydrated.hydra.core.config.Configuration;
import org.gethydrated.hydra.core.service.locator.ServiceLocator;
import org.gethydrated.hydra.core.service.locator.SystemServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages running services.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 * 
 */
public class ServiceManager {
    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ServiceManager.class);

    /**
     * Service locator.
     */
    private final ServiceLocator sl;
    
    /**
     * List of loaded services.
     */
    private final List<Service> services = new LinkedList<>();

    /**
     * Constructor.
     * @param cfg Configuration.
     */
    public ServiceManager(final Configuration cfg) {
        sl = new SystemServiceLocator(cfg);
    }

    /**
     * Attempts to start a service.
     * @param name service name.
     * @return unique service id.
     * @throws HydraException on failure.
     */
    public final Long startService(final String name) throws HydraException {
        try {
            ServiceInfo si = sl.locate(name);
            if (si != null) {
                Service s = new ServiceImpl(si);
                synchronized (this) {
                    services.add(s);
                }
                s.start();
                return s.getId();
            } else {
                throw new HydraException("Could not locate service with name: " + name);
            }
        } catch (IOException e) {
            throw new HydraException(e);
        }
    }

}
