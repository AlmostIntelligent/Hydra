package org.gethydrated.hydra.core.sid;

import java.util.concurrent.Future;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.refs.NullRef;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.USID;

/**
 * Temporary service id.
 */
public class TmpSID implements InternalSID {

    private final ActorRef ref;

    private final USID usid;

    /**
     * Constructor.
     * @param usid service usid.
     * @param ref actor ref.
     */
    public TmpSID(final USID usid, final ActorRef ref) {
        this.usid = usid;
        this.ref = ref;
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
    public void tell(final Object message, final SID sender) {
        if (sender == null) {
            ref.tell(message, new NullRef());
        } else {
            ref.tell(message, ((InternalSID) sender).getRef());
        }
    }

    @Override
    public Future<?> ask(final Object message) {
        return ref.ask(message);
    }

    @Override
    public String toString() {
        return "<0:2:" + usid.getServiceId() + ">";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof InternalSID)) {
            return false;
        }

        final InternalSID localSID = (InternalSID) o;

        return !(usid != null ? !usid.equals(localSID.getUSID()) : localSID
                .getUSID() != null);

    }

    @Override
    public int hashCode() {
        return usid != null ? usid.hashCode() : 0;
    }
}
