package org.gethydrated.hydra.core.io.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import org.gethydrated.hydra.core.io.transport.NodeAddress;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface NodeController {

    UUID getLocal();

    NodeAddress getConnector();

    UUID getUUID(int id);

    int getID(UUID nodeid);

    Set<UUID> getNodes();

    Map<UUID, NodeAddress> getNodesWithAddress();

    boolean isConnected(UUID node);

    boolean isConnected(int id);

    boolean addConnectingNode(UUID node);

    void removeConnectingNode(UUID node);

    boolean addConnectedNode(UUID node, final Channel channel, boolean force);

    boolean addKnownNode(UUID node, NodeAddress nodeAddress);

    void addKnownNodes(Map<UUID,NodeAddress> nodes, boolean flag);

    ObjectMapper defaultMapper();
}
