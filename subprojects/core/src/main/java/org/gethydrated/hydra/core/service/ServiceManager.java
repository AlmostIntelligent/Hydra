package org.gethydrated.hydra.core.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.service.Service;
import org.gethydrated.hydra.api.service.ServiceException;
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
    private static final Logger LOG = LoggerFactory
            .getLogger(ServiceManager.class);

    /**
     * Service locator.
     */
    private final ServiceLocator sl;

    /**
     * List of loaded services.
     */
    private final Map<Long, Service> services = new HashMap<>();

    /**
     * Configuration.
     */
    private final Configuration cfg;
    
    /**
     * Constructor.
     * 
     * @param cfg
     *            Configuration.
     */
    public ServiceManager(final Configuration cfg) {
        sl = new SystemServiceLocator(cfg);
        this.cfg = cfg;
    }

    /**
     * Attempts to start a service.
     * 
     * @param name
     *            service name.
     * @return unique service id.
     * @throws HydraException
     *             on failure.
     */
    public final Long startService(final String name) throws HydraException {
        try {
            ServiceInfo si = sl.locate(name);
            if (si != null) {
                Service s = new ServiceImpl(si, this, cfg);
                synchronized (this) {
                    services.put(null, s);
                }
                s.start();
                return s.getId();
            } else {
                throw new HydraException("Could not locate service with name: "
                        + name);
            }
        } catch (IOException e) {
            throw new HydraException(e);
        }
    }

    /**
     * Stops a service with a given id. If the id is not used,
     * the action will be ignored.
     * 
     * @param id Service id.
     * @throws HydraException on failure.
     */
    public final void stopService(final Long id) throws HydraException {
        Service s = services.get(id);
        if (s != null) {
            s.stop();
        }
    }

    /**
     * Stops all services.
     */
    public final void shutdown() {
        for (Service s : services.values()) {
            try {
                s.stop();
            } catch (ServiceException e) {
                LOG.info("{}", e);
            }
        }
    }

}
