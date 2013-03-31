package org.gethydrated.hydra.core.sid;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.refs.NullRef;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.USID;

import java.util.concurrent.Future;

/**
 *
 */
public class LocalSystemSID implements InternalSID {

    private ActorRef ref;

    private final USID usid;

    public LocalSystemSID(USID usid, ActorRef ref) {
        this.usid = usid;
        this.ref = ref;
    }

    @Override
    public USID getUSID() {
        return usid;
    }

    @Override
    public void tell(Object message, SID sender) {
        if(sender == null) {
            ref.tell(message, new NullRef());
        } else {
            ref.tell(message, ((InternalSID)sender).getRef());
        }
    }

    @Override
    public Future<?> ask(Object message) {
        return ref.ask(message);
    }

    @Override
    public String toString() {
        return "<0:1:" + usid.getServiceId() + ">";
    }

    @Override
    public ActorRef getRef() {
        return ref;
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
