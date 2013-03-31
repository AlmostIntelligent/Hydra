package org.gethydrated.hydra.api.event;

import org.gethydrated.hydra.api.service.USID;

/**
 *
 */
public class Unlink {
    private final USID usid;

    public Unlink(USID usid) {
        this.usid = usid;
    }

    public USID getUsid() {
        return usid;
    }
}
