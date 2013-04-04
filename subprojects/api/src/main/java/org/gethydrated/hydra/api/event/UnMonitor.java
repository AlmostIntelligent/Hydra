package org.gethydrated.hydra.api.event;

import org.gethydrated.hydra.api.service.USID;
import org.gethydrated.hydra.api.service.USIDAware;

/**
 *
 */
public class UnMonitor implements SystemEvent, USIDAware {
    private USID usid;

    public UnMonitor(USID usid) {
        this.usid = usid;
    }

    private UnMonitor() {}

    public USID getUSID() {
        return usid;
    }
}
