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

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class NodeConnector extends Actor {

    private final Configuration cfg;

    private ServerSocket serverSocket;

    private final IdMatcher idMatcher;

    public NodeConnector(Configuration cfg, IdMatcher idMatcher) {
        this.cfg = cfg;
        this.idMatcher = idMatcher;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof String) {
            switch ((String)message) {
                case "portchanged":
                    createServerSocket();
            }
        } else if(message instanceof ConnectTo) {
            connectNode((ConnectTo) message);
        } else if(message instanceof Connection) {
            createNodeActor((Connection) message);
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
        serverSocket.close();
    }

    private void connectNode(ConnectTo target) throws IOException {
        try {
            getLogger(NodeConnector.class).info("connecting to: " + target.ip + ":" + target.port);
            Socket s = new Socket(target.ip, target.port);

            Connection connection = new TCPConnection(s, idMatcher);
            connection.setConnector(new NodeAddress(InetAddress.getByName(target.ip).getHostAddress(), target.port));
            Map<UUID, NodeAddress> knownNodes =
                    connection.connect(new NodeAddress(InetAddress.getLocalHost().getHostAddress(), serverSocket.getLocalPort()));
            for(UUID key : knownNodes.keySet()) {
                if(!idMatcher.contains(key)) {
                    NodeAddress n = knownNodes.get(key);
                    ConnectTo c = new ConnectTo(n.getIp(),n.getPort());
                    getSelf().tell(c,getSelf());
                }
            }
            createNodeActor(connection);

            getSender().tell("ok", getSelf());
        }
        catch (IOException e) {
            getSender().tell(e,getSelf());
            throw e;
        } catch (IllegalArgumentException e) {
            getSender().tell(e,getSelf());
        }
    }

    private void createNodeActor(Connection connection) {
        idMatcher.addUUID(connection.getUUID());
        ActorRef nodes = getContext().getActor("/app/nodes");
        nodes.tell(connection, getSelf());
    }

    private void createServerSocket() throws ConfigItemNotFoundException, IOException {
        if(serverSocket != null) {
            serverSocket.close();
        }
        serverSocket = new ServerSocket(cfg.getInteger("network.port"));
        Thread t = new Thread(new SocketRunner(serverSocket));
        t.setDaemon(true);
        t.start();
    }

    private class SocketRunner implements Runnable {
        private ServerSocket ssocket;

        public SocketRunner(ServerSocket serverSocket) {
            ssocket = serverSocket;
        }

        @Override
        public void run() {
            while (!ssocket.isClosed()) {
                try {
                    Socket s = ssocket.accept();
                    Connection connection = new TCPConnection(s, idMatcher);
                    ActorRef ref = getContext().getActor("/app/nodes");
                    Future f = ref.ask("nodes");
                    connection.handshake((HashMap<UUID,NodeAddress>) f.get(10, TimeUnit.SECONDS));
                    getSelf().tell(connection, getSelf());
                } catch (Throwable e) {
                    getLogger(SocketRunner.class).error(e.getMessage() ,e);
                }
            }
        }
    }
}
