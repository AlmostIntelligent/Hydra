package org.gethydrated.hydra.api.event;

import org.gethydrated.hydra.api.service.USID;

/**
 *
 */
public class Unlink implements SystemEvent {
    private USID usid;

    public Unlink(USID usid) {
        this.usid = usid;
    }

    private Unlink() {}

    public USID getUsid() {
        return usid;
    }
}
