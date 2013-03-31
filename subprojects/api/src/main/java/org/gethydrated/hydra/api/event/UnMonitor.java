package org.gethydrated.hydra.api.event;

import org.gethydrated.hydra.api.service.USID;

/**
 *
 */
public class UnMonitor implements SystemEvent {
    private USID usid;

    public UnMonitor(USID usid) {
        this.usid = usid;
    }

    private UnMonitor() {}

    public USID getUsid() {
        return usid;
    }
}
