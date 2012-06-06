package org.gethydrated.hydra.core.api;

import org.gethydrated.hydra.api.HydraApi;
import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.core.service.ServiceManager;

/**
 * General Hydra api implementation.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 *
 */
public class HydraApiImpl implements HydraApi {

    /**
     * ServiceManager instance.
     */
    private final ServiceManager sm;
    
    /**
     * Constructor.
     * @param sm ServiceManager.
     */
    public HydraApiImpl(final ServiceManager sm) {
        this.sm = sm;
    }
    
    @Override
    public final Long startService(final String name) throws HydraException {
        return sm.startService(name);
    }

    @Override
    public final void stopService(final Long id) throws HydraException {
        sm.stopService(id);
    }
    
    /**
     * Getter for ServiceManager.
     * @return ServiceManager instance.
     */
    protected final ServiceManager getServiceManager() {
        return sm;
    }

}
