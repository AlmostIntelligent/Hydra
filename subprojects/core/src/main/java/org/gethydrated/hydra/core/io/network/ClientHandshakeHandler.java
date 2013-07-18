package org.gethydrated.hydra.core.io.network;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.gethydrated.hydra.core.io.transport.Envelope;
import org.gethydrated.hydra.core.io.transport.MessageType;
import org.gethydrated.hydra.core.io.transport.NodeAddress;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Client side handshake handler.
 * @author Christian Kulpa
 * @since 0.2.0
 */
public class ClientHandshakeHandler extends MessageToMessageDecoder<Envelope> {

    private ChannelHandlerContext ctx;
    private volatile boolean handshakeSuccess = false;
    private volatile boolean handshakeFailure = false;
    private volatile Throwable failureCause = null;
    private ScheduledFuture<?> timeout;

    private final NodeController nodeController;
    private final Queue<ChannelPromise> handshakeFutures = new LinkedList<>();
    private final Object lock = new Object();

    /**
     * Controller.
     * @param nodeController node controller.
     */
    public ClientHandshakeHandler(final NodeController nodeController) {
        this.nodeController = nodeController;
    }

    /**
     * Starts the handshake.
     * @return channel future for handshake result.
     */
    public ChannelFuture handshake() {
        return handshake(ctx.newPromise());
    }

    /**
     * Starts the handshake.
     * @param future for handshake result.
     * @return channel future for handshake result.
     */
    public ChannelFuture handshake(final ChannelPromise future) {
        if (handshakeSuccess) {
            future.setSuccess();
            return future;
        }
        if (handshakeFailure) {
            future.setFailure(failureCause);
            return future;
        }
        synchronized (lock) {
            if (handshakeSuccess) {
                future.setSuccess();
                return future;
            }
            if (handshakeFailure) {
                future.setFailure(failureCause);
                return future;
            }
            handshakeFutures.add(future);
            startHandshake();
            return future;
        }
    }

    private void startHandshake() {
        final Envelope env = new Envelope(MessageType.CONNECT);
        env.setSender(nodeController.getLocal());
        env.setCookie("nocookie");
        env.setConnector(nodeController.getConnector());
        ctx.writeAndFlush(env).syncUninterruptibly();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, Envelope msg, List<Object> out) throws Exception {
        if (handshakeFailure) {
            return;
        }
        if (handshakeSuccess) {
            out.add(msg);
            return;
        }
        synchronized (lock) {
            if (handshakeFailure) {
                return;
            }
            if (handshakeSuccess) {
                out.add(msg);
                return;
            }
            if (msg.getType() == MessageType.DECLINE) {
                setFailure(new RuntimeException(msg.getReason()));
                return;
            }
            if (msg.getType() == MessageType.ACCEPT) {
                final InetSocketAddress addr = (InetSocketAddress) ctx
                        .channel().localAddress();
                nodeController.addKnownNode(msg.getSender(), new NodeAddress(
                        addr.getAddress().getHostAddress(), msg.getConnector()
                                .getPort()));
                if (nodeController.addConnectedNode(msg.getSender(),
                        ctx.channel(), false)) {
                    final Envelope env = new Envelope(MessageType.ACK);
                    env.setSender(nodeController.getLocal());
                    env.setTarget(msg.getSender());
                    env.setNodes(nodeController.getNodesWithAddress());
                    ctx.writeAndFlush(env);
                    nodeController.addKnownNodes(msg.getNodes(), false);
                    setSuccess();
                } else {
                    final Envelope env = new Envelope(MessageType.DECLINE);
                    env.setSender(nodeController.getLocal());
                    env.setTarget(msg.getSender());
                    env.setReason("Concurrent connection attempt.");
                    ctx.writeAndFlush(env);
                    setFailure(new RuntimeException(
                            "Concurrent connection attempt."));
                }
            } else {
                System.out.println(msg);
            }
        }

    }

    @Override
    public void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        timeout = ctx.executor().schedule(new Runnable() {

            @Override
            public void run() {
                if (handshakeSuccess || handshakeFailure) {
                    return;
                }
                setFailure(new RuntimeException("Handshake timed out."));
            }
        }, 10000, TimeUnit.MILLISECONDS);
    }

    private void setFailure(final Throwable cause) {
        handshakeFailure = true;
        failureCause = cause;
        timeout.cancel(true);
        for (;;) {
            final ChannelPromise f = handshakeFutures.poll();
            if (f == null) {
                break;
            }
            f.setFailure(cause);
        }
        ctx.close();
    }

    private void setSuccess() {
        handshakeSuccess = true;
        timeout.cancel(true);
        for (;;) {
            final ChannelPromise f = handshakeFutures.poll();
            if (f == null) {
                break;
            }
            f.setSuccess();
        }
    }
}
