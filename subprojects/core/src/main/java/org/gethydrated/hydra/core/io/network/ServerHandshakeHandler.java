package org.gethydrated.hydra.core.io.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.gethydrated.hydra.core.io.transport.Envelope;
import org.gethydrated.hydra.core.io.transport.MessageType;
import org.gethydrated.hydra.core.io.transport.NodeAddress;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Server side handshake handler.
 * @author Christian Kulpa
 * @since 0.2.0
 */
public class ServerHandshakeHandler extends MessageToMessageDecoder<Envelope> {

    private ChannelHandlerContext ctx;
    private volatile boolean handshakeSuccess = false;
    private volatile boolean handshakeFailure = false;
    private ScheduledFuture<?> timeout;

    private final NodeController nodeController;
    private final Object lock = new Object();
    private int state = 0;

    /**
     * Constructor.
     * @param nodeController node controller.
     */
    public ServerHandshakeHandler(final NodeController nodeController) {
        this.nodeController = nodeController;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, Envelope msg, List<Object> out) throws Exception {
        if (handshakeSuccess) {
            out.add(msg);
            return;
        }
        if (handshakeFailure) {
            return;
        }
        synchronized (lock) {
            if (handshakeSuccess) {
                out.add(msg);
                return;
            }
            if (handshakeFailure) {
                return;
            }
            if (state == 0 && msg.getType() == MessageType.CONNECT) {
                handleConnectEnvelope(msg);
            } else if (state == 1 && msg.getType() == MessageType.ACK) {
                handleAckEnvelope(msg);
            } else if (state == 1 && msg.getType() == MessageType.DECLINE) {
                handleDeclineEnvelope(msg);
            } else {
                handleUnexpectedEnvelope(msg);
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
                ctx.close();
            }
        }, 10000, TimeUnit.MILLISECONDS);
    }

    private void handleConnectEnvelope(final Envelope envelope) {
        if (envelope.getSender().equals(nodeController.getLocal())) {
            final Envelope env = new Envelope(MessageType.DECLINE);
            env.setSender(nodeController.getLocal());
            env.setTarget(envelope.getSender());
            env.setReason("Cannot connect to self.");
            ctx.write(env).syncUninterruptibly();
            setFailure();
        } else {
            if (nodeController.addConnectingNode(envelope.getSender())) {
                final Envelope env = new Envelope(MessageType.ACCEPT);
                env.setSender(nodeController.getLocal());
                env.setTarget(envelope.getSender());
                env.setConnector(nodeController.getConnector());
                env.setNodes(nodeController.getNodesWithAddress());
                final InetSocketAddress addr = (InetSocketAddress) ctx
                        .channel().localAddress();
                nodeController.addKnownNode(envelope.getSender(),
                        new NodeAddress(addr.getAddress().getHostAddress(),
                                envelope.getConnector().getPort()));
                state++;
                ctx.writeAndFlush(env);
            } else {
                final Envelope env = new Envelope(MessageType.DECLINE);
                env.setSender(nodeController.getLocal());
                env.setTarget(envelope.getSender());
                env.setReason("Concurrent connection attempt.");
                ctx.writeAndFlush(env);
                setFailure();
            }
        }
    }

    private void handleAckEnvelope(final Envelope envelope) {
        if (nodeController.addConnectedNode(envelope.getSender(),
                ctx.channel(), true)) {
            nodeController.addKnownNodes(envelope.getNodes(), true);
            handshakeSuccess = true;
            timeout.cancel(true);
        } else {
            nodeController.removeConnectingNode(envelope.getSender());
        }
    }

    private void handleDeclineEnvelope(final Envelope envelope) {
        nodeController.removeConnectingNode(envelope.getSender());
        setFailure();
    }

    private void handleUnexpectedEnvelope(final Envelope envelope) {
        nodeController.removeConnectingNode(envelope.getSender());
        setFailure();
    }

    private void setFailure() {
        if (timeout != null) {
            timeout.cancel(true);
        }
        handshakeFailure = true;
        ctx.close();
    }
}
