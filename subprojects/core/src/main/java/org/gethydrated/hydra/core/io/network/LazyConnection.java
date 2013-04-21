package org.gethydrated.hydra.core.io.network;

import io.netty.channel.Channel;
import org.gethydrated.hydra.core.io.transport.Envelope;
import org.gethydrated.hydra.core.io.transport.NodeAddress;

import java.util.UUID;

/**
 *
 */
public class LazyConnection implements Connection {

    private final NodeAddress nodeAddress;
    private final int id;
    private final UUID uuid;

    private Channel channel;

    public LazyConnection(NodeAddress nodeAddress, int id, UUID uuid) {
        this.nodeAddress = nodeAddress;
        this.id = id;
        this.uuid = uuid;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public UUID uuid() {
        return uuid;
    }

    @Override
    public boolean isConnected() {
        return (channel != null) ? true : false;
    }

    @Override
    public Channel channel() {
        return channel();
    }

    @Override
    public void channel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public NodeAddress connector() {
        return nodeAddress;
    }

    @Override
    public void send(Envelope envelope) {
        channel.write(envelope).syncUninterruptibly();
    }
}
