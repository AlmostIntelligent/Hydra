package org.gethydrated.hydra.core.sid;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.refs.NullRef;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.USID;

import java.util.concurrent.Future;

/**
 *
 */
public class DeadSID implements InternalSID {

    private final USID usid;

    public DeadSID(USID usid) {
        this.usid = usid;
    }

    @Override
    public ActorRef getRef() {
        return new NullRef();
    }

    @Override
    public USID getUSID() {
        return usid;
    }

    @Override
    public void tell(Object message, SID sender) {
    }

    @Override
    public Future<?> ask(Object message) {
        throw new RuntimeException("Actor not found for usid: " + usid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof InternalSID)) return false;

        InternalSID localSID = (InternalSID) o;

        return !(usid != null ? !usid.equals(localSID.getUSID()) : localSID.getUSID() != null);

    }

    @Override
    public int hashCode() {
        return usid != null ? usid.hashCode() : 0;
    }
}
