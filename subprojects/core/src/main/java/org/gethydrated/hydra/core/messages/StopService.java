package org.gethydrated.hydra.core.messages;

import org.gethydrated.hydra.api.event.SystemEvent;
import org.gethydrated.hydra.api.service.USID;
import org.gethydrated.hydra.core.sid.InternalSID;

/**
 *
 */
public class StopService implements SystemEvent {
    private USID usid;

    public StopService(USID usid) {
        this.usid = usid;
    }

    private StopService() {}

    public USID getUsid() {
        return usid;
    }
}
