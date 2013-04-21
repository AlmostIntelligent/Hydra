package org.gethydrated.hydra.core.io.network;

import io.netty.channel.Channel;
import org.gethydrated.hydra.core.io.transport.Envelope;
import org.gethydrated.hydra.core.io.transport.NodeAddress;

import java.util.UUID;

/**
 *
 */
public interface Connection {

    int id();

    UUID uuid();

    boolean isConnected();

    Channel channel();

    void channel(Channel channel);

    NodeAddress connector();

    void send(Envelope envelope);
}
