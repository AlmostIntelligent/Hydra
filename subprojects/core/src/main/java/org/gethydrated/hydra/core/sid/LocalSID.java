package org.gethydrated.hydra.core.sid;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.FutureImpl;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.USID;

import java.util.concurrent.Future;

public class LocalSID implements SID {

    private ActorRef ref;

    public LocalSID(ActorRef ref) {
        this.ref = ref;
    }

    @Override
    public USID getUSID() {
        return null;
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
        return "<0:0:"+ref.getName()+">";
    }

}
