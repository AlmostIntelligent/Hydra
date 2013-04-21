package org.gethydrated.hydra.core.io.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.core.io.transport.Envelope;

/**
 *
 */
public class ActorHandler extends ChannelInboundMessageHandlerAdapter<Envelope> {

    private final ActorRef nodeActor;

    public ActorHandler(ActorRef nodeActor) {
        this.nodeActor = nodeActor;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, Envelope msg) throws Exception {
        nodeActor.tell(msg, null);
    }
}
