package org.gethydrated.hydra.core.io.network;

import io.netty.channel.Channel;
import org.gethydrated.hydra.core.io.transport.Envelope;
import org.gethydrated.hydra.core.io.transport.NodeAddress;

import java.util.UUID;

/**
 * Node connection interface.
 */
public interface Connection {

    /**
     * Returns the node id.
     * @return node id.
     */
    int id();

    /**
     * Returns the node uuid.
     * @return node uuid.
     */
    UUID uuid();

    /**
     * Returns the connection state.
     * @return true, if connected.
     */
    boolean isConnected();

    /**
     * Returns the connection channel.
     * @return channel.
     */
    Channel channel();

    /**
     * Sets the connection channel.
     * @param channel netty channel.
     */
    void channel(Channel channel);

    /**
     * Returns the node address.
     * @return node address.
     */
    NodeAddress connector();

    /**
     * Sends an Envelope to the other node.
     * @param envelope Envelope.
     */
    void send(Envelope envelope);

    void addCloseListener(Runnable listener);
}
