package org.gethydrated.hydra.core.sid;

import java.util.UUID;
import java.util.concurrent.Future;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.event.SystemEvent;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.USID;
import org.gethydrated.hydra.core.io.transport.SerializedObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * Local user service id.
 * @author Chris
 *
 */
public final class LocalSID implements InternalSID {

    private final ActorRef ref;

    private final USID usid;

    private final ObjectWriter writer;

    /**
     * Default constructor.
     * @param nodeId    Hydra node id.
     * @param ref       Actorref of the target service actor.
     * @param writer    Object serializer.
     */
    public LocalSID(final UUID nodeId, final ActorRef ref,
            final ObjectWriter writer) {
        this.ref = ref;
        this.writer = writer;
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
    public void tell(final Object m, final SID sender) {
        Object message = m;
        if (!(message instanceof SystemEvent)
                && !(message instanceof SerializedObject)) {
            final SerializedObject so = new SerializedObject();
            so.setSender(sender.getUSID());
            so.setTarget(usid);
            so.setClassName(message.getClass().getName());
            try {
                so.setData(writer.writeValueAsBytes(message));
            } catch (final JsonProcessingException e) {
                e.printStackTrace();
            }
            message = so;
        }
        if (sender == null) {
            ref.tell(message, ref);
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
        return "<0:" + usid.getTypeId() + ":" + usid.getServiceId() + ">";
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
