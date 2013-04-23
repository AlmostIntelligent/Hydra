package org.gethydrated.hydra.core.sid;

import java.util.concurrent.Future;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.event.SystemEvent;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.USID;
import org.gethydrated.hydra.core.io.transport.SerializedObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * Service identifier used for services on other
 * Hydra nodes.
 */
public final class ForeignSID implements InternalSID {

    private final USID usid;

    private final ActorRef ref;

    private final int nodeId;

    private final ObjectWriter writer;

    /**
     * Default constructor.
     * @param nodeId    Hydra node id.
     * @param usid      Unique service id.
     * @param ref       Actorref of the io actor.
     * @param writer    Object serializer.
     */
    public ForeignSID(final int nodeId, final USID usid, final ActorRef ref,
            final ObjectWriter writer) {
        this.ref = ref;
        this.usid = usid;
        this.nodeId = nodeId;
        this.writer = writer;
    }

    @Override
    public USID getUSID() {
        return usid;
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
        return null;
    }

    @Override
    public ActorRef getRef() {
        return ref;
    }

    @Override
    public String toString() {
        return "<" + nodeId + ":" + usid.getTypeId() + ":"
                + usid.getServiceId() + ">";
    }
}
