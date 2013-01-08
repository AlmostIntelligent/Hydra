package org.gethydrated.hydra.core.api;

import org.gethydrated.hydra.api.HydraApi;
import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.core.service.Services;

/**
 * General Hydra api implementation.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 *
 */
public class HydraApiImpl implements HydraApi {
    
    /**
     * Constructor.
     */
    public HydraApiImpl() {
    }
    
    @Override
    public final Long startService(final String name) throws HydraException {
        return 0L;
    }

    @Override
    public final void stopService(final Long id) throws HydraException {
    }

}
