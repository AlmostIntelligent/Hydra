package org.gethydrated.hydra.core.transport;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 *
 */
public class EnvelopeSerializer extends JsonSerializer<Envelope> {
    @Override
    public void serialize(Envelope envelope, JsonGenerator jGen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jGen.writeStartObject();
        jGen.writeStringField("type", String.valueOf(envelope.getType()));
        jGen.writeStringField("sender", envelope.getSender().toString());
        if(envelope.getType() != MessageType.CONNECT) {
            jGen.writeStringField("target", envelope.getTarget().toString());
        } else {
            jGen.writeStringField("target", null);
        }
        switch (envelope.getType()) {
            case CONNECT:
                jGen.writeStringField("secureCookie", envelope.getCookie());
                jGen.writeFieldName("connector");
                jGen.writeObject(envelope.getConnector());
                break;
            case ACCEPT:
                jGen.writeFieldName("nodes");
                jGen.writeObject(envelope.getNodes());
                break;
            case DECLINE:
                jGen.writeStringField("reason", envelope.getReason());
                break;
            case DISCONNECT:
                break;
            case SYSTEM:
                jGen.writeFieldName("sobject");
                jGen.writeObject(envelope.getSObject());
                break;
        }
        jGen.writeEndObject();
    }
}
