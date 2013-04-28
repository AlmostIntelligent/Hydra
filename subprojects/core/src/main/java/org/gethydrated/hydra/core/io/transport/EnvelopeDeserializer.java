package org.gethydrated.hydra.core.io.transport;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 *
 */
public class EnvelopeDeserializer extends JsonDeserializer<Envelope> {
    @Override
    public Envelope deserialize(final JsonParser jsonParser,
            final DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            throw new IllegalStateException("Expected START_OBJECT token, got "
                    + jsonParser.getCurrentToken().name() + " instead");
        }
        jsonParser.nextToken();
        final MessageType type = parseMessageType(jsonParser);
        final Envelope envelope = new Envelope(type);
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            switch (jsonParser.getCurrentName()) {
            case "sender":
                jsonParser.nextToken();
                envelope.setSender(UUID.fromString(jsonParser
                        .getValueAsString()));
                break;
            case "target":
                jsonParser.nextToken();
                if (type != MessageType.CONNECT) {
                    final String uuid = jsonParser.getValueAsString();
                    envelope.setTarget(UUID.fromString(uuid));
                }
                break;
            case "secureCookie":
                jsonParser.nextToken();
                if (type == MessageType.CONNECT) {
                    envelope.setCookie(jsonParser.getValueAsString());
                }
                break;
            case "hidden":
                jsonParser.nextToken();
                if (type == MessageType.CONNECT) {
                    envelope.setHiddenNode(jsonParser.getValueAsBoolean());
                }
                break;
            case "connector":
                jsonParser.nextToken();
                if (type == MessageType.CONNECT || type == MessageType.ACCEPT) {
                    envelope.setConnector(jsonParser
                            .readValueAs(NodeAddress.class));
                }
                break;
            case "nodes":
                jsonParser.nextToken();
                if (type == MessageType.ACCEPT || type == MessageType.ACK
                        || type == MessageType.NODES) {
                    final Map<UUID, NodeAddress> uuids = parseNodeMap(jsonParser);
                    envelope.setNodes(uuids);
                }
                break;
            case "reason":
                jsonParser.nextToken();
                if (type == MessageType.DECLINE) {
                    envelope.setReason(jsonParser.getValueAsString());
                }
                break;
            case "sobject":
                jsonParser.nextToken();
                if (type == MessageType.SYSTEM || type == MessageType.USER) {
                    envelope.setSObject(jsonParser
                            .readValueAs(SerializedObject.class));
                }
                break;
            case "timestamp":
                jsonParser.nextToken();
                if (type == MessageType.SYSTEM || type == MessageType.USER) {
                    envelope.setTimestamp(jsonParser.getValueAsLong());
                }
                break;
            default:
                break;

            }
        }
        return envelope;
    }

    private Map<UUID, NodeAddress> parseNodeMap(final JsonParser jsonParser)
            throws IOException {
        if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            throw new IllegalStateException("Expected START_OBJECT token, got "
                    + jsonParser.getCurrentToken() + " instead.");
        }
        final Map<UUID, NodeAddress> uuids = new HashMap<>();
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            final UUID id = UUID.fromString(jsonParser.getCurrentName());
            jsonParser.nextToken();
            final NodeAddress addr = jsonParser.readValueAs(NodeAddress.class);
            uuids.put(id, addr);
        }
        return uuids;
    }

    private MessageType parseMessageType(final JsonParser jsonParser)
            throws IOException {
        if (!jsonParser.getCurrentName().equals("type")) {
            throw new IllegalStateException("Expected type field first, got "
                    + jsonParser.getCurrentName() + " field instead.");
        }
        jsonParser.nextToken();
        final String type = jsonParser.getValueAsString();
        return MessageType.valueOf(type);
    }
}
