package org.gethydrated.hydra.core.sid;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.USID;

import java.util.concurrent.Future;

/**
 *
 */
public class ForeignSID implements SID {

    private final USID usid;

    private final ActorRef ref;

    public ForeignSID(USID usid, ActorRef ref) {
        this.ref = ref;
        this.usid = usid;
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
        return null;
    }
}
