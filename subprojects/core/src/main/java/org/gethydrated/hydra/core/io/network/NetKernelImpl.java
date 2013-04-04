package org.gethydrated.hydra.core.io.network;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.core.transport.Envelope;
import org.gethydrated.hydra.core.transport.EnvelopeModule;
import org.gethydrated.hydra.core.transport.MessageType;

import java.io.IOException;

public class NetKernelImpl implements NetKernel {

    private final Configuration cfg;
    private boolean running = false;
    private final ObjectMapper objectMapper;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private NodeController nodeController = new NodeController();
    private ActorSystem actorSystem;
    private ChannelGroup channels = new DefaultChannelGroup("hydra-channels");

    private ServerBootstrap serverBootstrap;

    public NetKernelImpl(Configuration cfg, ActorSystem actorSystem) throws Exception {
        this.cfg = cfg;
        this.actorSystem = actorSystem;
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.registerModule(new EnvelopeModule());
        objectMapper.registerModule(new JaxbAnnotationModule());
        if (cfg.getInteger("network.port") != 0) {
            bind(cfg.getInteger("network.pork"));
        }
    }

    @Override
    public void bind(int port) throws IOException {
        if (!running) {
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
            serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(final SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new JSONEncoder(objectMapper),
                                    new JSONDecoder(objectMapper),
                                    new ServerHandshakeHandler(actorSystem, nodeController, channels));
                        }
                    });
            serverBootstrap.localAddress(port);
            try {
                ChannelFuture f = serverBootstrap.bind().sync();
                running = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close() {
        if (running) {
            try {
                running = false;
                channels.close();
                ChannelGroupFuture f = channels.close();
                f.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                bossGroup.shutdown();
                workerGroup.shutdown();
            }
        }
    }

    @Override
    public synchronized void connect(final String ip, final int port) throws IOException {
        if (running) {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(final SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new JSONEncoder(objectMapper),
                                    new JSONDecoder(objectMapper),
                                    new ClientHandshakeHandler(actorSystem, nodeController));
                        }
                    });

            bootstrap.remoteAddress(ip, port);
            ChannelFuture f = null;
            try {
                f = bootstrap.connect().sync();
            } catch (InterruptedException e) {
                try {
                    f.channel().close().sync();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            channels.add(f.channel());
        }
    }
}
