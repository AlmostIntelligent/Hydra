package org.gethydrated.hydra.core.io.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
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
public class EnvelopeHandler extends
        ChannelInboundMessageHandlerAdapter<Envelope> {

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
    public void messageReceived(final ChannelHandlerContext ctx,
            final Envelope msg) throws Exception {
        if (msg.getType() == MessageType.NODES) {
            nodeController.addKnownNodes(msg.getNodes(), false);
        } else if (msg.getType() == MessageType.DISCONNECT) {
            nodeController.removeNode(msg.getSender());
        } else {
            ioActor.tell(msg, null);
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx,
            final Throwable cause) throws Exception {
        logger.warn(cause.getMessage(), cause);
        ctx.channel().close();
    }
}