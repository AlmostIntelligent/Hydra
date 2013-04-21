package org.gethydrated.hydra.core.io.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 *
 */
public class ChannelInitializerFactory {

    private final ObjectMapper mapper;
    private final NodeController nodeController;

    public ChannelInitializerFactory(NodeController nodeController, ObjectMapper mapper) {
        this.mapper = mapper;
        this.nodeController = nodeController;
    }

    public ChannelInitializer<SocketChannel> createServerInitializer() {
        return createInitializer(true);
    }

    public ChannelInitializer<SocketChannel> createClientInitializer() {
        return createInitializer(false);
    }

    private ChannelInitializer<SocketChannel> createInitializer(final boolean isServerChannel) {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new JSONDecoder(mapper));
                pipeline.addLast(new JSONEncoder(mapper));
                if (isServerChannel) {
                    pipeline.addLast(new ServerHandshakeHandler(nodeController));
                } else {
                    pipeline.addLast(new ClientHandshakeHandler(nodeController));
                }
                pipeline.addLast(new EnvelopeHandler(nodeController));
            }
        };
    }
}
