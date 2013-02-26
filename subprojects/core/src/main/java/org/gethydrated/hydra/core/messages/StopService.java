package org.gethydrated.hydra.core.messages;

import org.gethydrated.hydra.core.sid.InternalSID;

/**
 *
 */
public class StopService {
    public final InternalSID sid;

    public StopService(InternalSID sid) {
        this.sid = sid;
    }
}
