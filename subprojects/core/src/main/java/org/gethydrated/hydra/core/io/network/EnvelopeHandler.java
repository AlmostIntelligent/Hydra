package org.gethydrated.hydra.core.io.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import org.gethydrated.hydra.core.transport.Envelope;

public class EnvelopeHandler extends ChannelInboundMessageHandlerAdapter<Envelope> {
    @Override
    public void messageReceived(final ChannelHandlerContext ctx, final Envelope msg) throws Exception {
        System.out.println(msg);
    }
}
