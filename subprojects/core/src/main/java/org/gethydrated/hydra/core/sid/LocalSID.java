package org.gethydrated.hydra.core.sid;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.FutureImpl;
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
    public ActorRef getRef() {
        return ref;
    }

    @Override
    public void tell(Object message, SID sender) {
        if(sender == null) {
            ref.tell(message, null);
        } else {
            ref.tell(message, ((LocalSID)sender).ref);
        }
    }

    @Override
    public Future<?> ask(Object message) {
        FutureImpl<Object> future = new FutureImpl<>();
        ref.tell(message, future);
        return future;
    }

    public String toString() {
        return "<0:" + usid.typeId + ":" + usid.serviceId + ">";
    }

}
