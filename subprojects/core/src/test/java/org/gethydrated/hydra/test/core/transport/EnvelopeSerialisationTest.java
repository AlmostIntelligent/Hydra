package org.gethydrated.hydra.test.core.transport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.gethydrated.hydra.core.transport.Envelope;
import org.gethydrated.hydra.core.transport.EnvelopeModule;
import org.gethydrated.hydra.core.transport.MessageType;
import org.gethydrated.hydra.core.transport.NodeAddress;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 *
 */
public class EnvelopeSerialisationTest {

    private static final UUID senderUUID = UUID.randomUUID();

    private static final UUID targetUUID = UUID.randomUUID();

    private static final NodeAddress nodeAddress = new NodeAddress("localhost", 8080);

    private static final String declineReason = "TestReason";

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public static void setup() {
        mapper.registerModule(new EnvelopeModule());
    }

    @Test
    public void testConnectSerialization() throws JsonProcessingException {
        Envelope env = createConnectEnvelope();
        String serialized = mapper.writeValueAsString(env);
        assertNotNull(serialized);
        assertTrue(serialized.matches("\\{\"type\":\"CONNECT\",\"sender\":\"[0-f]+-[0-f]+-[0-f]+-[0-f]+-[0-f]+\",\"target\":null,\"secureCookie\":\"nocookie\",\"connector\":\\{\"ip\":\"localhost\",\"port\":8080}}"));
    }

    @Test
    public void testConnectDeserialization() throws IOException {
        Envelope env = createConnectEnvelope();
        String serialized = mapper.writeValueAsString(env);
        assertNotNull(serialized);
        Envelope env2 = mapper.readValue(serialized, Envelope.class);
        assertNotNull(env2);
        assertEquals(env, env2);
    }

    @Test
    public void testDeclineSerialization() throws JsonProcessingException {
        Envelope env = createDeclineEnvelope();
        String serialized = mapper.writeValueAsString(env);
        assertNotNull(serialized);
        assertTrue(serialized.matches("\\{\"type\":\"DECLINE\",\"sender\":\"[0-f]+-[0-f]+-[0-f]+-[0-f]+-[0-f]+\",\"target\":\"[0-f]+-[0-f]+-[0-f]+-[0-f]+-[0-f]+\",\"reason\":\""+ declineReason +"\"}"));
    }

    @Test
    public void testAcceptSerializationEmptyList() throws JsonProcessingException {
        Envelope env = createAcceptEnvelope();
        String serialized = mapper.writeValueAsString(env);
        assertNotNull(serialized);
    }

    @Test
    public void testAcceptSerialization() throws JsonProcessingException {
        Envelope env = createAcceptEnvelope();
        for(int i=0; i < 0 ; i++) {
            env.getNodes().put(senderUUID, nodeAddress);
        }
        String serialized = mapper.writeValueAsString(env);
        assertNotNull(serialized);
    }

    private Envelope createConnectEnvelope() {
        Envelope env = new Envelope(MessageType.CONNECT);
        env.setSender(senderUUID);
        env.setCookie("nocookie");
        env.setConnector(nodeAddress);
        return env;
    }

    private Envelope createDeclineEnvelope() {
        Envelope env = new Envelope(MessageType.DECLINE);
        env.setSender(senderUUID);
        env.setTarget(targetUUID);
        env.setReason(declineReason);
        return env;
    }

    private Envelope createAcceptEnvelope() {
        Envelope env = new Envelope(MessageType.ACCEPT);
        env.setSender(senderUUID);
        env.setTarget(targetUUID);
        env.setNodes(new HashMap<UUID, NodeAddress>());
        return env;
    }
}
