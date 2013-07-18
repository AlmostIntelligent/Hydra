package org.gethydrated.hydra.core.io.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.core.io.transport.Envelope;
import org.gethydrated.hydra.core.io.transport.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Envelope handler. Bridge between netty and the
 * actor system.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 */
public class EnvelopeHandler extends ChannelInboundHandlerAdapter {

    private final NodeController nodeController;

    private ActorRef ioActor;

    private final Logger logger = LoggerFactory
            .getLogger(EnvelopeHandler.class);

    /**
     * Constructor.
     * @param nodeController node controller.
     */
    public EnvelopeHandler(final NodeController nodeController) {
        this.nodeController = nodeController;
    }

    /**
     * Sets an io actor for this handler. This will be set by the
     * server oder client handlers.
     * @param ioActor io actor.
     */
    public void setActorRef(final ActorRef ioActor) {
        this.ioActor = ioActor;
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx,
            final Object msg) throws Exception {
        if (msg instanceof Envelope) {
            Envelope env = (Envelope) msg;
            if (env.getType() == MessageType.NODES) {
                nodeController.addKnownNodes(env.getNodes(), false);
            } else if (env.getType() == MessageType.DISCONNECT) {
                nodeController.removeNode(env.getSender());
            } else {
                ioActor.tell(msg, null);
            }
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx,
            final Throwable cause) throws Exception {
        logger.warn(cause.getMessage(), cause);
        ctx.channel().close();
    }
}