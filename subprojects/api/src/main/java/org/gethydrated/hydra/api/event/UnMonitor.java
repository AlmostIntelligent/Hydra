package org.gethydrated.hydra.api.event;

import org.gethydrated.hydra.api.service.USID;

/**
 *
 */
public class UnMonitor {
    private final USID usid;

    public UnMonitor(USID usid) {
        this.usid = usid;
    }

    public USID getUsid() {
        return usid;
    }
}
