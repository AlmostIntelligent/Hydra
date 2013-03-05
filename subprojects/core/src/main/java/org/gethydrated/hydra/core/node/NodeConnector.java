package org.gethydrated.hydra.core.node;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.core.messages.ConnectTo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class NodeConnector extends Actor {

    private Configuration cfg;

    private ServerSocket serverSocket;

    public NodeConnector(Configuration cfg) {
        this.cfg = cfg;
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
        } else if(message instanceof Socket) {
            handShake((Socket)message);
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
            BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream()));
            int newId = Integer.parseInt(r.readLine());
            int id = Integer.parseInt(r.readLine());
            ActorRef nodes = getContext().getActor("/app/nodes");
            nodes.tell(new Connection(s, id), getSelf());
            getSender().tell(""+id, getSelf());
        }
        catch (IOException e) {
            getSender().tell(e,getSelf());
            throw e;
        }

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

    private void handShake(Socket socket) throws InterruptedException, ExecutionException, TimeoutException, IOException {
        ActorRef coordinator = getContext().getActor("/app/coordinator");
        Future fnew = coordinator.ask("newNodeId");
        Future fown = coordinator.ask("ownId");
        int newId = (int) fnew.get(1, TimeUnit.SECONDS);
        int ownId = (int) fown.get(1, TimeUnit.SECONDS);
        PrintStream p = new PrintStream(socket.getOutputStream());
        p.println(newId);
        p.println(ownId);
        ActorRef nodes = getContext().getActor("/app/nodes");
        nodes.tell(new Connection(socket, newId), getSelf());
    }

    private void newConnectionCallback(Socket s) {
        getSelf().tell(s, getSelf());
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
                    newConnectionCallback(s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
