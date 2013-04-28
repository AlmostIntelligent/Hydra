package org.gethydrated.hydra.core.api;

import org.gethydrated.hydra.api.Hydra;
import org.gethydrated.hydra.api.HydraApi;
import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.USID;

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
     * 
     * @param hydra Hydra instance.
     */
    public HydraApiImpl(final Hydra hydra) {
        this.hydra = hydra;
    }

    @Override
    public final SID startService(final String name) throws HydraException {
        return hydra.startService(name);
    }

    @Override
    public SID getService(final String name) {
        return hydra.getService(name);
    }

    @Override
    public SID getService(final USID usid) {
        return hydra.getService(usid);
    }

    @Override
    public final void stopService(final SID id) throws HydraException {
        hydra.stopService(id);
    }

}
