package org.gethydrated.hydra.core.sid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.event.SystemEvent;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.USID;
import org.gethydrated.hydra.core.transport.SerializedObject;

import java.util.concurrent.Future;

/**
 *
 */
public class ForeignSID implements InternalSID {

    private final USID usid;

    private final ActorRef ref;

    private final int nodeId;

    private ObjectWriter writer;

    public ForeignSID(int nodeId, USID usid, ActorRef ref, ObjectWriter writer) {
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
    public void tell(Object message, SID sender) {
        if (!(message instanceof SystemEvent) && !(message instanceof SerializedObject)) {
            SerializedObject so = new SerializedObject();
            so.setSender(sender.getUSID());
            so.setTarget(usid);
            so.setClassName(message.getClass().getName());
            try {
                so.setData(writer.writeValueAsBytes(message));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            message = so;
        }
        if(sender == null) {
            ref.tell(message, ref);
        } else {
            ref.tell(message, ((InternalSID) sender).getRef());
        }
    }

    @Override
    public Future<?> ask(Object message) {
        return null;
    }

    @Override
    public ActorRef getRef() {
        return null;
    }

    @Override
    public String toString() {
        return "<"+ nodeId + ":" + usid.getTypeId()+ ":" + usid.getServiceId() + ">";
    }
}
