package org.gethydrated.hydra.core.sid;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.USID;

import java.util.concurrent.Future;

/**
 *
 */
public class LocalSystemSID implements SID {

    private final int systemId;

    private ActorRef ref;

    public LocalSystemSID(int systemId, ActorRef ref) {
        this.systemId = systemId;
        this.ref = ref;
    }

    @Override
    public USID getUSID() {
        return null;
    }

    @Override
    public void tell(Object message, SID sender) {

    }

    @Override
    public Future<?> ask(Object message) {
        return null;
    }

    public String toString() {
        return "<0:1:" + systemId + ">";
    }
}
