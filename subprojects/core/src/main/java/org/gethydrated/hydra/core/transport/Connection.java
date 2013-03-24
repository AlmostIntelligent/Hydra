package org.gethydrated.hydra.core.transport;

import org.gethydrated.hydra.actors.ActorRef;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.UUID;

/**
 *
 */
public interface Connection {

    public Map<UUID, NodeAddress> connect(NodeAddress connectorAddress) throws IOException;

    public boolean handshake(Map<UUID, NodeAddress> nodes) throws IOException;

    public void disconnect();

    public void setReceiveCallback(ActorRef target);

    public UUID getUUID();

    public InetAddress getIp();

    public int getPort();

    NodeAddress getConnector();

    void setConnector(NodeAddress addr);
}
