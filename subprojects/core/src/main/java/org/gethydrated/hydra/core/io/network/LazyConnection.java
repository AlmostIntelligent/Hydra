package org.gethydrated.hydra.core.io.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.gethydrated.hydra.core.io.transport.Envelope;
import org.gethydrated.hydra.core.io.transport.NodeAddress;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Lazy node connection.
 */
public class LazyConnection implements Connection {

    private final NodeAddress nodeAddress;
    private final int id;
    private final UUID uuid;
    private final NetKernel netKernel;
    private final Set<Runnable> listeners = new HashSet<>();
    private final Object lock = new Object();
    private volatile boolean closed = false;

    private Channel channel;

    /**
     * Constructor.
     * @param nodeAddress node address.
     * @param id node id.
     * @param uuid node uuid.
     */
    public LazyConnection(final NodeAddress nodeAddress, final int id,
            final UUID uuid, final NetKernel netKernel) {
        this.nodeAddress = nodeAddress;
        this.id = id;
        this.uuid = uuid;
        this.netKernel = netKernel;
        listeners.add(new Runnable() {
            @Override
            public void run() {
                netKernel.removeNode(uuid);
            }
        });
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
        return (channel != null);
    }

    @Override
    public Channel channel() {
        return channel;
    }

    @Override
    public void channel(final Channel chan) {
        System.err.println("set channel: " + uuid);
        this.channel = chan;
        synchronized (lock) {
            lock.notifyAll();
        }
        channel.closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture future) throws Exception {
                channel = null;
                closed = true;
                for (Runnable r : listeners) {
                    r.run();
                }
            }
        });
    }

    @Override
    public NodeAddress connector() {
        return nodeAddress;
    }

    @Override
    public void send(final Envelope envelope) {
        if (!closed) {
            if (channel == null) {
                try {
                    netKernel.connect(nodeAddress.getIp(), nodeAddress.getPort());
                } catch (Exception e) {
                    if (!e.getMessage().equals("Concurrent connection attempt.")) {
                        netKernel.removeNode(uuid);
                        closed = true;
                        for (Runnable r : listeners) {
                            r.run();
                        }
                    }
                }
                synchronized (lock) {
                    if (channel == null) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            channel.writeAndFlush(envelope).syncUninterruptibly();
        }
    }

    @Override
    public void addCloseListener(Runnable listener) {
        if (closed) {
            listener.run();
        } else {
            listeners.add(listener);
        }
    }
}
