package org.gethydrated.hydra.core.api;

import org.gethydrated.hydra.api.Hydra;
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

    private final Hydra hydra;

    /**
     * Constructor.
     * @param hydra
     */
    public HydraApiImpl(Hydra hydra) {
        this.hydra = hydra;
    }
    
    @Override
    public final SID startService(final String name) throws HydraException {
        return hydra.startService(name);
    }

    @Override
    public final void stopService(final SID id) throws HydraException {
        hydra.stopService(id);
    }

}
