package org.gethydrated.hydra.api.event;

import org.gethydrated.hydra.api.service.USID;
import org.gethydrated.hydra.api.service.USIDAware;

/**
 *
 */
public class ServiceExit implements SystemEvent, USIDAware {
    private USID usid;

    private String reason;

    public ServiceExit(USID usid, String reason) {
        this.usid = usid;
        this.reason = reason;
    }

    private ServiceExit() {}

    public USID getUSID() {
        return usid;
    }

    public String getReason() {
        return reason;
    }
}
