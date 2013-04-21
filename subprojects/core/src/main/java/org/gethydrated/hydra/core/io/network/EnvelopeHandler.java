package org.gethydrated.hydra.core.io.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import org.gethydrated.hydra.core.io.transport.Envelope;
import org.gethydrated.hydra.core.io.transport.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnvelopeHandler extends ChannelInboundMessageHandlerAdapter<Envelope> {

    private final NodeController nodeController;

    private final Logger logger = LoggerFactory.getLogger(EnvelopeHandler.class);

    public EnvelopeHandler(NodeController nodeController) {
        this.nodeController = nodeController;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, Envelope msg) throws Exception {
        System.out.println("EnvelopeHandler got: " + msg);
        if (msg.getType() == MessageType.NODES) {
            nodeController.addKnownNodes(msg.getNodes(), false);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn(cause.getMessage(), cause);
        ctx.channel().close();
    }
}