package org.gethydrated.hydra.core.transport;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.*;

/**
 *
 */
public class EnvelopeDeserializer extends JsonDeserializer<Envelope> {
    @Override
    public Envelope deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            throw new IllegalStateException("Expected START_OBJECT token, got " + jsonParser.getCurrentToken().name() + " instead");
        }
        jsonParser.nextToken();
        MessageType type = parseMessageType(jsonParser);
        Envelope envelope = new Envelope(type);
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            switch (jsonParser.getCurrentName()) {
                case "sender":
                    jsonParser.nextToken();
                    envelope.setSender(UUID.fromString(jsonParser.getValueAsString()));
                    break;
                case "target":
                    jsonParser.nextToken();
                    if(type != MessageType.CONNECT) {
                        String uuid = jsonParser.getValueAsString();
                        envelope.setTarget(UUID.fromString(uuid));
                    }
                    break;
                case "secureCookie":
                    jsonParser.nextToken();
                    if(type == MessageType.CONNECT) {
                        envelope.setCookie(jsonParser.getValueAsString());
                    }
                    break;
                case "hidden":
                    jsonParser.nextToken();
                    if(type == MessageType.CONNECT) {
                        envelope.setHiddenNode(jsonParser.getValueAsBoolean());
                    }
                    break;
                case "connector":
                    jsonParser.nextToken();
                    if(type == MessageType.CONNECT) {
                        envelope.setConnector(jsonParser.readValueAs(NodeAddress.class));
                    }
                    break;
                case "nodes":
                    jsonParser.nextToken();
                    if(type == MessageType.ACCEPT) {
                        Map<UUID,NodeAddress> uuids = parseNodeMap(jsonParser);
                        envelope.setNodes(uuids);
                    }
                    break;
                case "reason":
                    jsonParser.nextToken();
                    if(type == MessageType.DECLINE) {
                        envelope.setReason(jsonParser.getValueAsString());
                    }
                    break;
                case "sobject":
                    jsonParser.nextToken();
                    if(type == MessageType.SYSTEM) {
                        envelope.setSObject(jsonParser.readValueAs(SerializedObject.class));
                    }
                    break;
            }
        }
        return envelope;
    }

    private Map<UUID, NodeAddress> parseNodeMap(JsonParser jsonParser) throws IOException {
        if(jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            throw new IllegalStateException("Expected START_OBJECT token, got " + jsonParser.getCurrentToken() + " instead.");
        }
        Map<UUID, NodeAddress> uuids = new HashMap<>();
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            UUID id = UUID.fromString(jsonParser.getCurrentName());
            jsonParser.nextToken();
            NodeAddress addr = jsonParser.readValueAs(NodeAddress.class);
            uuids.put(id, addr);
        }
        return uuids;
    }

    private MessageType parseMessageType(JsonParser jsonParser) throws IOException {
        if(!jsonParser.getCurrentName().equals("type")) {
            throw new IllegalStateException("Expected type field first, got " + jsonParser.getCurrentName() + " field instead.");
        }
        jsonParser.nextToken();
        String type = jsonParser.getValueAsString();
        return MessageType.valueOf(type);
    }
}
