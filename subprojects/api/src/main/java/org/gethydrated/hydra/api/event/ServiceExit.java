package org.gethydrated.hydra.api.event;

import org.gethydrated.hydra.api.service.USID;

/**
 *
 */
public class ServiceExit {
    private USID sid;

    private String reason;

    public ServiceExit(USID sid, String reason) {
        this.sid = sid;
        this.reason = reason;
    }

    public USID getUsid() {
        return sid;
    }

    public String getReason() {
        return reason;
    }
}
