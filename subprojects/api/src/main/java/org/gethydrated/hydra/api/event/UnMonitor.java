package org.gethydrated.hydra.api.event;

import org.gethydrated.hydra.api.service.USID;
import org.gethydrated.hydra.api.service.USIDAware;

/**
 * UnMonitor event.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 */
public class UnMonitor implements SystemEvent, USIDAware {
    private USID usid;

    /**
     * Constructor.
     * @param usid service usid.
     */
    public UnMonitor(final USID usid) {
        this.usid = usid;
    }

    @SuppressWarnings("unused")
    private UnMonitor() {
    }

    @Override
    public USID getUSID() {
        return usid;
    }
}
