package org.gethydrated.hydra.core.api;

import org.gethydrated.hydra.api.HydraApi;
import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.service.SID;

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
    public final SID startService(final String name) throws HydraException {
        return null;
    }

    @Override
    public final void stopService(final SID id) throws HydraException {
    }

}
