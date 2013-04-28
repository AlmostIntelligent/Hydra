package org.gethydrated.hydra.test.core.transport;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.gethydrated.hydra.core.io.transport.Envelope;
import org.gethydrated.hydra.core.io.transport.EnvelopeModule;
import org.gethydrated.hydra.core.io.transport.MessageType;
import org.gethydrated.hydra.core.io.transport.NodeAddress;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 */
public class EnvelopeSerialisationTest {

    private static final UUID SENDERUUID = UUID.randomUUID();

    private static final UUID TARGETUUID = UUID.randomUUID();

    private static final NodeAddress NODEADDRESS = new NodeAddress("localhost",
            8080);

    private static final String DECLINEREASON = "TestReason";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @BeforeClass
    public static void setup() {
        MAPPER.registerModule(new EnvelopeModule());
    }

    @Test
    public void testConnectSerialization() throws JsonProcessingException {
        final Envelope env = createConnectEnvelope();
        final String serialized = MAPPER.writeValueAsString(env);
        assertNotNull(serialized);
        assertTrue(serialized
                .matches("\\{\"type\":\"CONNECT\",\"sender\":\"[0-f]+-[0-f]+-[0-f]+-[0-f]+-[0-f]+\",\"target\":null,\"secureCookie\":\"nocookie\",\"hidden\":\"false\",\"connector\":\\{\"ip\":\"localhost\",\"port\":8080}}"));
    }

    @Test
    public void testConnectDeserialization() throws IOException {
        final Envelope env = createConnectEnvelope();
        final String serialized = MAPPER.writeValueAsString(env);
        assertNotNull(serialized);
        final Envelope env2 = MAPPER.readValue(serialized, Envelope.class);
        assertNotNull(env2);
        assertEquals(env, env2);
    }

    @Test
    public void testDeclineSerialization() throws JsonProcessingException {
        final Envelope env = createDeclineEnvelope();
        final String serialized = MAPPER.writeValueAsString(env);
        assertNotNull(serialized);
        assertTrue(serialized
                .matches("\\{\"type\":\"DECLINE\",\"sender\":\"[0-f]+-[0-f]+-[0-f]+-[0-f]+-[0-f]+\",\"target\":\"[0-f]+-[0-f]+-[0-f]+-[0-f]+-[0-f]+\",\"reason\":\""
                        + DECLINEREASON + "\"}"));
    }

    @Test
    public void testAcceptSerializationEmptyList()
            throws JsonProcessingException {
        final Envelope env = createAcceptEnvelope();
        final String serialized = MAPPER.writeValueAsString(env);
        assertNotNull(serialized);
    }

    @Test
    public void testAcceptSerialization() throws JsonProcessingException {
        final Envelope env = createAcceptEnvelope();
        for (int i = 0; i < 0; i++) {
            env.getNodes().put(SENDERUUID, NODEADDRESS);
        }
        final String serialized = MAPPER.writeValueAsString(env);
        assertNotNull(serialized);
    }

    private Envelope createConnectEnvelope() {
        final Envelope env = new Envelope(MessageType.CONNECT);
        env.setSender(SENDERUUID);
        env.setCookie("nocookie");
        env.setConnector(NODEADDRESS);
        return env;
    }

    private Envelope createDeclineEnvelope() {
        final Envelope env = new Envelope(MessageType.DECLINE);
        env.setSender(SENDERUUID);
        env.setTarget(TARGETUUID);
        env.setReason(DECLINEREASON);
        return env;
    }

    private Envelope createAcceptEnvelope() {
        final Envelope env = new Envelope(MessageType.ACCEPT);
        env.setSender(SENDERUUID);
        env.setTarget(TARGETUUID);
        env.setNodes(new HashMap<UUID, NodeAddress>());
        return env;
    }
}
