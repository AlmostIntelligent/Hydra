package org.gethydrated.hydra.core.sid;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.USID;

import java.util.concurrent.Future;

/**
 *
 */
public class ForeignSID implements InternalSID {

    private final USID usid;

    private final ActorRef ref;

    private final int nodeId;

    public ForeignSID(int nodeId, USID usid, ActorRef ref) {
        this.ref = ref;
        this.usid = usid;
        this.nodeId = nodeId;
    }

    @Override
    public USID getUSID() {
        return usid;
    }

    @Override
    public void tell(Object message) {
        tell(message, this);
    }

    @Override
    public void tell(Object message, SID sender) {

    }

    @Override
    public Future<?> ask(Object message) {
        return null;
    }

    @Override
    public ActorRef getRef() {
        return null;
    }

    @Override
    public String toString() {
        return "<"+ nodeId + ":" + usid.getTypeId()+ ":" + usid.getServiceId() + ">";
    }
}
