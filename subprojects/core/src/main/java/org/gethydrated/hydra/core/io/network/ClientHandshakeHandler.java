package org.gethydrated.hydra.core.io.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.core.transport.Envelope;
import org.gethydrated.hydra.core.transport.MessageType;

public class ClientHandshakeHandler extends ChannelInboundMessageHandlerAdapter<Envelope> {

    private final NodeController nodeController;

    public ClientHandshakeHandler(final ActorSystem actorSystem, NodeController nodeController) {
        this.nodeController = nodeController;
    }

    @Override
    public void messageReceived(final ChannelHandlerContext ctx, final Envelope msg) throws Exception {
        System.out.println(ClientHandshakeHandler.class);
        if(msg.getType() == MessageType.DECLINE) {
            ctx.close();
            throw new Exception(msg.getReason());
        }
    }

    @Override
    public void	channelActive(ChannelHandlerContext ctx) {
        System.out.println("channel registered");
        Envelope env = new Envelope(MessageType.CONNECT);
        env.setSender(nodeController.getLocal());
        env.setCookie("no-cookie");
        env.setConnector(nodeController.getConnector());
        ctx.channel().write(env);
    }
}
