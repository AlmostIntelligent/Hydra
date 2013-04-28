package org.gethydrated.hydra.core.service;

import org.gethydrated.hydra.api.event.SystemEvent;
import org.gethydrated.hydra.api.service.USID;

/**
 * Service stop message.
 */
public class StopService implements SystemEvent {
    private USID usid;

    /**
     * Constructor.
     * @param usid service usid.
     */
    public StopService(final USID usid) {
        this.usid = usid;
    }

    @SuppressWarnings("unused")
    private StopService() {
    }

    /**
     * Returns the service usid.
     * @return service usid.
     */
    public USID getUsid() {
        return usid;
    }
}
