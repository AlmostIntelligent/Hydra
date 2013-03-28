package org.gethydrated.hydra.core.sid;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.USID;

import java.util.UUID;
import java.util.concurrent.Future;

public class LocalSID implements InternalSID {

    private final ActorRef ref;

    private final USID usid;

    public LocalSID(UUID nodeId, ActorRef ref) {
       this.ref = ref;
       usid = new USID(nodeId, 0, Long.parseLong(ref.getName()));
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
    public ActorRef getRef() {
        return ref;
    }

    @Override
    public void tell(Object message, SID sender) {
        if(sender == null) {
            ref.tell(message, ref);
        } else {
            ref.tell(message, ((InternalSID) sender).getRef());
        }
    }

    @Override
    public Future<?> ask(Object message) {
        return ref.ask(message);
    }

    public String toString() {
        return "<0:" + usid.getTypeId() + ":" + usid.getServiceId() + ">";
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
