package org.gethydrated.hydra.core.messages;

import org.gethydrated.hydra.api.service.SID;

/**
 *
 */
public class ServiceUp {

    private final SID sid;

    public ServiceUp(SID sid) {
        this.sid = sid;
    }

    public SID getSid() {
        return sid;
    }
}
