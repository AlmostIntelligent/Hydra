package org.gethydrated.hydra.api.event;

import org.gethydrated.hydra.api.service.USID;

/**
 *
 */
public class ServiceExit implements SystemEvent {
    private USID sid;

    private String reason;

    public ServiceExit(USID sid, String reason) {
        this.sid = sid;
        this.reason = reason;
    }

    private ServiceExit() {}

    public USID getUsid() {
        return sid;
    }

    public String getReason() {
        return reason;
    }
}
