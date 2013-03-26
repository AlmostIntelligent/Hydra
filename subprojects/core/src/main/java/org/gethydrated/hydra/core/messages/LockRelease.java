package org.gethydrated.hydra.core.messages;

import org.gethydrated.hydra.api.service.SID;

/**
 *
 */
public class LockRelease {
    private final SID sid;

    public LockRelease(SID sid) {
        this.sid = sid;
    }

    public SID getSid() {
        return sid;
    }
}
