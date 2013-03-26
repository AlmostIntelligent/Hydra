package org.gethydrated.hydra.core.messages;

import org.gethydrated.hydra.api.service.SID;

/**
 *
 */
public class LockRequest {
    private final SID sid;

    public LockRequest(SID sid) {
        this.sid = sid;
    }

    public SID getSid() {
        return sid;
    }
}
