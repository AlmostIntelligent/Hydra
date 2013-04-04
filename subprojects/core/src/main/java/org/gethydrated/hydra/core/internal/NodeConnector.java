package org.gethydrated.hydra.core.internal;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.core.messages.ConnectTo;
import org.gethydrated.hydra.core.sid.IdMatcher;
import org.gethydrated.hydra.core.transport.Connection;
import org.gethydrated.hydra.core.transport.NodeAddress;
import org.gethydrated.hydra.core.transport.TCPConnection;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class NodeConnector extends Actor {

    private final Configuration cfg;

    private ServerSocket serverSocket;

    private final IdMatcher idMatcher;

    private final Logger logger = getLogger(NodeConnector.class);

    private final ActorRef nodes;

    public NodeConnector(Configuration cfg, IdMatcher idMatcher) {
        this.cfg = cfg;
        this.idMatcher = idMatcher;
        nodes = getContext().getActor("/app/nodes");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        try {
        if(message instanceof String) {
            switch ((String)message) {
                case "portchanged":
                    createServerSocket();
            }
        } else if(message instanceof ConnectTo) {
            try {
                nodes.tell("pauseIO", getSelf());
                connectNode((ConnectTo) message);
                getSender().tell("ok", getSelf());
            } catch (Exception e) {
                logger.error("{}", e.getMessage(), e);
            } finally {
                nodes.tell("resumeIO", getSelf());
            }

        }
        } catch (ConfigItemNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() throws ConfigItemNotFoundException, IOException {
        if(cfg.getInteger("network.port")!=0) {
            createServerSocket();
        }
    }

    @Override
    public void onStop() throws IOException {
        if(serverSocket != null) {
            serverSocket.close();
        }
    }

    private synchronized void connectNode(ConnectTo target) throws IOException, ConfigItemNotFoundException {
        Map<UUID, NodeAddress> knownNodes = getNodes();
        Socket s = new Socket();
        s.connect(new InetSocketAddress(target.ip, target.port),cfg.getInteger("network.timeout-connect"));
        s.setSoTimeout(cfg.getInteger("network.timeout-read"));
        s.setKeepAlive(cfg.getBoolean("network.keep-alive"));
        Connection connection = new TCPConnection(s, idMatcher);
        connection.setConnector(new NodeAddress(InetAddress.getByName(target.ip).getHostAddress(), target.port));
        try {
            Map<UUID, NodeAddress> newNodes =
                connection.connect(new NodeAddress(InetAddress.getLocalHost().getHostAddress(), serverSocket.getLocalPort()), getNodes());
            createNodeActor(connection);
            for (Map.Entry<UUID, NodeAddress> na : newNodes.entrySet()) {
                if(!na.getKey().equals(idMatcher.getLocal())) {
                    ConnectTo ct = new ConnectTo(na.getValue().getIp(), na.getValue().getPort());
                    connectNode(ct);
                }
            }
        } catch (IllegalArgumentException e) {
            //skip
        }
    }

    private void createNodeActor(Connection connection) {
        if (!idMatcher.contains(connection.getUUID())) {
            idMatcher.addUUID(connection.getUUID());
            ActorRef nodes = getContext().getActor("/app/nodes");
            nodes.tell(connection, getSelf());
        }
    }

    private void createServerSocket() throws ConfigItemNotFoundException, IOException {
        if(serverSocket != null) {
            serverSocket.close();
        }
        try {
            serverSocket = new ServerSocket(cfg.getInteger("network.port"));
            Thread t = new Thread(new SocketRunner(serverSocket));
            t.setDaemon(true);
            t.start();
        } catch (IOException e) {
            getLogger(NodeConnector.class).warn("{}", e.getMessage(), e);
        }
    }

    private Map<UUID, NodeAddress> getNodes() {
        ActorRef ref = getContext().getActor("/app/nodes");
        Future f = ref.ask("nodes");
        try {
            return (Map<UUID, NodeAddress>) f.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            getLogger(NodeConnector.class).warn("{}", e.getMessage(), e);
            return new HashMap<>();
        }
    }

    private class SocketRunner implements Runnable {
        private ServerSocket ssocket;

        public SocketRunner(ServerSocket serverSocket) {
            ssocket = serverSocket;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void run() {
            while (!ssocket.isClosed()) {
                try {
                    Socket s = ssocket.accept();
                    s.setSoTimeout(cfg.getInteger("network.timeout-read"));
                    s.setKeepAlive(cfg.getBoolean("network.keep-alive"));
                    nodes.tell("pauseIO", getSelf());
                    Connection connection = new TCPConnection(s, idMatcher);
                    Map<UUID, NodeAddress> otherNodes = connection.handshake(getNodes());
                    createNodeActor(connection);
                    for (Map.Entry<UUID, NodeAddress> na : otherNodes.entrySet()) {
                        if (!na.getKey().equals(idMatcher.getLocal())) {
                            ConnectTo ct = new ConnectTo(na.getValue().getIp(), na.getValue().getPort());
                            connectNode(ct);
                        }
                    }
                } catch (IOException e) {
                    // skip.
                } catch (Throwable e) {
                    getLogger(SocketRunner.class).error(e.getMessage() ,e);
                } finally {
                    nodes.tell("resumeIO", getSelf());
                }
            }
        }
    }
}
