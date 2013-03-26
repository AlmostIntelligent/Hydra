package org.gethydrated.hydra.core.messages;

import org.gethydrated.hydra.api.service.SID;

/**
 *
 */
public class ServiceDown {
    private final SID sid;

    public ServiceDown(SID sid) {
        this.sid = sid;
    }

    public SID getSid() {
        return sid;
    }
}
