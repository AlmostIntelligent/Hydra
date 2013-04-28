package org.gethydrated.hydra.core.sid;

import java.util.concurrent.Future;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.refs.NullRef;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.USID;

/**
 * Dead Service Identifier. Can be used as
 * return value on failed service lookups.
 */
public final class DeadSID implements InternalSID {

    private final USID usid;

    /**
     * Default constructor.
     * @param usid
     *          Dead USID.
     */
    public DeadSID(final USID usid) {
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
    public void tell(final Object message, final SID sender) {
    }

    @Override
    public Future<?> ask(final Object message) {
        throw new RuntimeException("Actor not found for usid: " + usid);
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
