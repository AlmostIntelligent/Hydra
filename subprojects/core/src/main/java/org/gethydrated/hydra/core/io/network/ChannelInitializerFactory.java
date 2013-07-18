package org.gethydrated.hydra.core.io.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * Channel pipeline factory.
 */
public class ChannelInitializerFactory {

    private final ObjectMapper mapper;
    private final NodeController nodeController;

    /**
     * Constructor.
     * @param nodeController node controller.
     * @param mapper object mapper.
     */
    public ChannelInitializerFactory(final NodeController nodeController,
            final ObjectMapper mapper) {
        this.mapper = mapper;
        this.nodeController = nodeController;
    }

    /**
     * Creates a server pipeline.
     * @return server channel pipeline.
     */
    public ChannelInitializer<SocketChannel> createServerInitializer() {
        return createInitializer(true);
    }

    /**
     * Creates a client pipeline.
     * @return client channel pipeline.
     */
    public ChannelInitializer<SocketChannel> createClientInitializer() {
        return createInitializer(false);
    }

    private ChannelInitializer<SocketChannel> createInitializer(
            final boolean isServerChannel) {

        return new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(final SocketChannel ch) throws Exception {
                final ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new JSONCodec(mapper));
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
