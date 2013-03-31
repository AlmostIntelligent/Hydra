package org.gethydrated.hydra.api.event;

import org.gethydrated.hydra.api.service.USID;

/**
 *
 */
public class ServiceDown implements SystemEvent {
    private USID sid;

    private String reason;

    public ServiceDown(USID sid, String reason) {
        this.sid = sid;
        this.reason = reason;
    }

    private ServiceDown() {}

    public USID getUsid() {
        return sid;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "ServiceDown{" +
                "sid=" + sid +
                ", reason='" + reason + '\'' +
                '}';
    }
}
