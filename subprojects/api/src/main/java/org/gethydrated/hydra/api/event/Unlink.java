package org.gethydrated.hydra.api.event;

import org.gethydrated.hydra.api.service.USID;
import org.gethydrated.hydra.api.service.USIDAware;

/**
 * Unlink event.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 */
public class Unlink implements SystemEvent, USIDAware {
    private USID usid;

    /**
     * Constructor.
     * @param usid service usid.
     */
    public Unlink(final USID usid) {
        this.usid = usid;
    }

    @SuppressWarnings("unused")
    private Unlink() {
    }

    @Override
    public USID getUSID() {
        return usid;
    }
}
