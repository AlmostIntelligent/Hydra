package org.gethydrated.hydra.api.event;

import org.gethydrated.hydra.api.service.USID;
import org.gethydrated.hydra.api.service.USIDAware;

/**
 *
 */
public class Unlink implements SystemEvent, USIDAware {
    private USID usid;

    public Unlink(USID usid) {
        this.usid = usid;
    }

    private Unlink() {}

    public USID getUSID() {
        return usid;
    }
}
