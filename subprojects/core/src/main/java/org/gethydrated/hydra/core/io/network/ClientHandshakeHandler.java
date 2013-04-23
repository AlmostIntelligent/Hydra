package org.gethydrated.hydra.core.io.network;

import io.netty.channel.*;
import org.eclipse.jetty.util.ArrayQueue;
import org.gethydrated.hydra.core.io.transport.Envelope;
import org.gethydrated.hydra.core.io.transport.MessageType;
import org.gethydrated.hydra.core.io.transport.NodeAddress;

import java.net.InetSocketAddress;
import java.util.Queue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Client side handshake handler.
 * @author Christian Kulpa
 * @since 0.2.0
 */
public class ClientHandshakeHandler extends
        ChannelInboundMessageHandlerAdapter<Envelope> {

    private ChannelHandlerContext ctx;
    private volatile boolean handshakeSuccess = false;
    private volatile boolean handshakeFailure = false;
    private volatile Throwable failureCause = null;
    private ScheduledFuture<?> timeout;

    private final NodeController nodeController;
    private final Queue<ChannelPromise> handshakeFutures = new ArrayQueue<>();
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
        ctx.write(env).syncUninterruptibly();
    }

    @Override
    public void messageReceived(final ChannelHandlerContext ctx,
            final Envelope msg) throws Exception {
        if (handshakeFailure) {
            return;
        }
        if (handshakeSuccess) {
            ChannelHandlerUtil.addToNextInboundBuffer(ctx, msg);
            ctx.fireInboundBufferUpdated();
            return;
        }
        synchronized (lock) {
            if (handshakeFailure) {
                return;
            }
            if (handshakeSuccess) {
                ChannelHandlerUtil.addToNextInboundBuffer(ctx, msg);
                ctx.fireInboundBufferUpdated();
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
                    ctx.write(env).syncUninterruptibly();
                    nodeController.addKnownNodes(msg.getNodes(), false);
                    setSuccess();
                } else {
                    final Envelope env = new Envelope(MessageType.DECLINE);
                    env.setSender(nodeController.getLocal());
                    env.setTarget(msg.getSender());
                    env.setReason("Concurrent connection attempt.");
                    ctx.write(env).syncUninterruptibly();
                    setFailure(new RuntimeException(
                            "Concurrent connection attempt."));
                }
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
        timeout.cancel(true);
        handshakeFailure = true;
        failureCause = cause;
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
        timeout.cancel(true);
        handshakeSuccess = true;
        for (;;) {
            final ChannelPromise f = handshakeFutures.poll();
            if (f == null) {
                break;
            }
            f.setSuccess();
        }
    }
}
