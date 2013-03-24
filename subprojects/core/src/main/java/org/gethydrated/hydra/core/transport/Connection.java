package org.gethydrated.hydra.core.transport;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gethydrated.hydra.actors.ActorRef;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

/**
 *
 */
public interface Connection {

    Map<UUID, NodeAddress> connect(NodeAddress connectorAddress) throws IOException;

    boolean handshake(Map<UUID, NodeAddress> nodes) throws IOException;

    void disconnect() throws IOException;

    void sendEnvelope(Envelope envelope) throws IOException;

    void setReceiveCallback(ActorRef target, ExecutorService executorService);

    NodeAddress getConnector();

    void setConnector(NodeAddress addr);

    UUID getUUID();

    InetAddress getIp();

    int getPort();

    ObjectMapper getMapper();

    boolean isConnected();

    boolean isClosed() throws IOException;
}
