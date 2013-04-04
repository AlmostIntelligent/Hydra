package org.gethydrated.hydra.api.event;

import org.gethydrated.hydra.api.service.USID;
import org.gethydrated.hydra.api.service.USIDAware;

/**
 *
 */
public class ServiceDown implements SystemEvent, USIDAware {
    private USID usid;

    private String reason;

    public ServiceDown(USID usid, String reason) {
        this.usid = usid;
        this.reason = reason;
    }

    private ServiceDown() {}

    public USID getUSID() {
        return usid;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "ServiceDown{" +
                "usid=" + usid +
                ", reason='" + reason + '\'' +
                '}';
    }
}
