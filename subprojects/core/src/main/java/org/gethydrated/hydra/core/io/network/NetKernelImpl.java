package org.gethydrated.hydra.core.io.network;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.io.transport.Envelope;
import org.gethydrated.hydra.core.io.transport.EnvelopeModule;
import org.gethydrated.hydra.core.io.transport.MessageType;
import org.gethydrated.hydra.core.io.transport.NodeAddress;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Net kernel implementation.
 * @author Christian Kulpa
 * @since 0.2.0
 */
public final class NetKernelImpl implements NetKernel {

    private boolean running = false;

    private final ObjectMapper objectMapper;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private final ChannelGroup channels = new DefaultChannelGroup(
            "hydra-channels");
    private ServerBootstrap serverBootstrap;

    private final InternalHydra hydra;

    private final Lock lock = new ReentrantLock();

    private final UUID localNode = UUID.randomUUID();
    private NodeAddress localNodeAddress;
    private final ConcurrentHashMap<UUID, Connection> knownNodes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, UUID> idMatches = new ConcurrentHashMap<>();

    private final ConcurrentHashSet<UUID> connectingNodes = new ConcurrentHashSet<>();

    private final AtomicInteger nodeId = new AtomicInteger(0);
    private final ChannelInitializerFactory initializerFactory;

    /**
     * Constructor.
     * @param hydra Internal Hydra representation. 
     * @throws Exception on failure.
     */
    public NetKernelImpl(final InternalHydra hydra) throws Exception {
        this.hydra = hydra;
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.registerModule(new EnvelopeModule());
        objectMapper.registerModule(new JaxbAnnotationModule());
        if (hydra.getConfiguration().getInteger("network.port") != 0) {
            bind(hydra.getConfiguration().getInteger("network.pork"));
        }
        initializerFactory = new ChannelInitializerFactory(this, objectMapper);
    }

    @Override
    public synchronized void bind(final int port) throws IOException {
        if (!running) {
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
            serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(initializerFactory.createServerInitializer());
            serverBootstrap.option(ChannelOption.SO_REUSEADDR, false);
            try {
                final ChannelFuture f = serverBootstrap.bind(port)
                        .syncUninterruptibly();
                final InetSocketAddress addr = (InetSocketAddress) f.channel()
                        .localAddress();
                localNodeAddress = new NodeAddress(addr.getAddress()
                        .getHostAddress(), addr.getPort());
                running = true;
            } catch (final Exception e) {
                bossGroup.shutdown();
                workerGroup.shutdown();
                throw new IOException(e);
            }
        }
    }

    @Override
    public void close() {
        if (running) {
            try {
                running = false;
                channels.close();
                final ChannelGroupFuture f = channels.close();
                f.await();
            } catch (final InterruptedException e) {
                e.printStackTrace();
            } finally {
                bossGroup.shutdown();
                workerGroup.shutdown();
            }
        }
    }

    @Override
    public void connect(final String ip, final int port) {
        if (running) {
            final Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup).channel(NioSocketChannel.class)
                    .handler(initializerFactory.createClientInitializer());

            bootstrap.remoteAddress(ip, port);
            final ChannelFuture f = bootstrap.connect().syncUninterruptibly();
            channels.add(f.channel());
            final ChannelFuture handshakeFuture = f.channel().pipeline()
                    .get(ClientHandshakeHandler.class).handshake();
            handshakeFuture.syncUninterruptibly();
        } else {
            throw new RuntimeException(
                    "Node not active. Please set a connection port via port command.");
        }
    }

    @Override
    public UUID getLocal() {
        return localNode;
    }

    @Override
    public NodeAddress getConnector() {
        return localNodeAddress;
    }

    @Override
    public UUID getUUID(final int id) {
        if (id == 0) {
            return localNode;
        }
        return idMatches.get(id);
    }

    @Override
    public int getID(final UUID nodeid) {
        if (!knownNodes.containsKey(nodeid)) {
            return -1;
        }
        return knownNodes.get(nodeid).id();
    }

    @Override
    public Set<UUID> getNodes() {
        return new HashSet<>(knownNodes.keySet());
    }

    @Override
    public Map<UUID, NodeAddress> getNodesWithAddress() {
        final Map<UUID, NodeAddress> n = new HashMap<>();
        for (final Entry<UUID, Connection> e : knownNodes.entrySet()) {
            n.put(e.getKey(), e.getValue().connector());
        }
        return n;
    }

    @Override
    public boolean isConnected(final UUID node) {
        final Connection c = knownNodes.get(node);
        return (c != null && c.isConnected());
    }

    @Override
    public boolean isConnected(final int id) {
        return false;
    }

    @Override
    public boolean addConnectingNode(final UUID node) {
        lock.lock();
        try {
            final boolean b = isConnected(node);
            if (connectingNodes.contains(node) || b) {
                return false;
            } else {
                connectingNodes.add(node);
                return true;
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void removeConnectingNode(final UUID node) {
        connectingNodes.remove(node);
    }

    @Override
    public boolean addConnectedNode(final UUID node, final Channel channel,
            final boolean force) {
        lock.lock();
        try {
            if (!force && connectingNodes.contains(node)
                    && (node.compareTo(localNode) < 0)) {
                return false;
            }
            connectingNodes.remove(node);
            final Connection con = knownNodes.get(node);
            con.channel(channel);
            EnvelopeHandler eh = channel.pipeline().get(EnvelopeHandler.class);
            ActorRef ref = hydra.getActorSystem().getActor("/app/nodes/" + con.id());
            eh.setActorRef(ref);
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public synchronized boolean addKnownNode(final UUID uuid, final NodeAddress nodeAddress) {
        try {
            if (!uuid.equals(localNode) && !knownNodes.containsKey(uuid)) {
                LazyConnection lc = new LazyConnection(nodeAddress, nodeId.incrementAndGet(), uuid, this);
                knownNodes.put(uuid, lc);
                idMatches.put(nodeId.get(), uuid);
                ActorRef ref = hydra.getActorSystem().getActor("/app/nodes");
                Future<?> f = ref.ask(lc);
                f.get(10, TimeUnit.SECONDS);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void addKnownNodes(final Map<UUID, NodeAddress> nodes, final boolean force) {
        boolean flag = false;
        for (final Entry<UUID, NodeAddress> e : nodes.entrySet()) {
            flag |= addKnownNode(e.getKey(), e.getValue());
        }
        if (flag || force) {
            updateNodes();
        }
    }

    @Override
    public void removeNode(UUID node) {
        knownNodes.remove(node);
        idMatches.values().remove(node);
    }

    @Override
    public ObjectMapper defaultMapper() {
        return objectMapper;
    }

    private void updateNodes() {
        final Map<UUID, NodeAddress> n = getNodesWithAddress();
        for (final Connection c : knownNodes.values()) {
            if (c.isConnected()) {
                final Envelope env = new Envelope(MessageType.NODES);
                env.setSender(localNode);
                env.setTarget(c.uuid());
                env.setNodes(n);
                c.send(env);
            }
        }
    }
}
