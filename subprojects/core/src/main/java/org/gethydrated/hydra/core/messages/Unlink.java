package org.gethydrated.hydra.core.messages;

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
