package org.gethydrated.hydra.core.io.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.core.transport.Envelope;
import org.gethydrated.hydra.core.transport.MessageType;

public class ServerHandshakeHandler extends ChannelInboundMessageHandlerAdapter<Envelope> {

    private final NodeController nodeController;

    public ServerHandshakeHandler(final ActorSystem actorSystem, NodeController nodeController, final ChannelGroup channels) {
        this.nodeController = nodeController;
    }

    @Override
    public void messageReceived(final ChannelHandlerContext ctx, final Envelope msg) throws Exception {
        System.out.println(ServerHandshakeHandler.class);
        if(msg.getType() != MessageType.CONNECT) {
            ctx.close();
        }
        if (msg.getSender() == null || nodeController.isKnownNode(msg.getSender())) {
            Envelope env = new Envelope(MessageType.DECLINE);
            env.setSender(nodeController.getLocal());
            env.setTarget(msg.getSender());
            env.setReason("UUID already in use: " + msg.getSender());
            ctx.write(env);
            ctx.close();
        }
        nodeController.addNode(msg.getSender(), msg.getConnector());
    }
}

//
