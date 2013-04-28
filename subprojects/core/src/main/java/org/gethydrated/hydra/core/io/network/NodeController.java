package org.gethydrated.hydra.core.io.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import org.gethydrated.hydra.core.io.transport.NodeAddress;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Node controller interface.
 * @author Christian Kulpa
 * @since 0.2.0
 */
public interface NodeController {

    /**
     * Returns the local node uuid.
     * @return local uuid.
     */
    UUID getLocal();

    /**
     * Returns the local node address.
     * @return local node address.
     */
    NodeAddress getConnector();

    /**
     * Returns the uuid to the given id.
     * @param id node id.
     * @return node uuid.
     */
    UUID getUUID(int id);

    /**
     * Returns the id to the given uuid.
     * @param nodeid node uuid.
     * @return node id.
     */
    int getID(UUID nodeid);

    /**
     * Returns a set containing all known nodes.
     * @return known nodes.
     */
    Set<UUID> getNodes();

    /**
     * Returns all known nodes with node connectors.
     * @return nodes and node connectors.
     */
    Map<UUID, NodeAddress> getNodesWithAddress();

    /**
     * Returns if the given node uuid is connected.
     * @param node node uuid.
     * @return true if the node is connected.
     */
    boolean isConnected(UUID node);

    /**
     * Returns if the given node id is connected.
     * @param id node id.
     * @return true if the node is connected.
     */
    boolean isConnected(int id);

    /**
     * Adds a connecting node.
     * @param node node uuid.
     * @return true if the node could be added.
     */
    boolean addConnectingNode(UUID node);

    /**
     * Removes the node from the connectiong node list.
     * @param node node uuid.
     */
    void removeConnectingNode(UUID node);

    /**
     * Sets the given node uuid as connected node.
     * @param node node uuid.
     * @param channel node connection channel.
     * @param force forces the node processing.
     * @return true if the node wasn't in the list of connected nodes.
     */
    boolean addConnectedNode(UUID node, final Channel channel, boolean force);

    /**
     * Adds a node to the list of known nodes.
     * @param node node uuid.
     * @param nodeAddress node address.
     * @return true if the node wasn't already in the list of known nodes.
     */
    boolean addKnownNode(UUID node, NodeAddress nodeAddress);

    /**
     * Adds all nodes to the list of known nodes.
     * @param nodes node list.
     * @param force forces the node processing. 
     */
    void addKnownNodes(Map<UUID, NodeAddress> nodes, boolean force);

    void removeNode(UUID node);

    /**
     * Returns the default object mapper.
     * @return object mapper.
     */
    ObjectMapper defaultMapper();
}
