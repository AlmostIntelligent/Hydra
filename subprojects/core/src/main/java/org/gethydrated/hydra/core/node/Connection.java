package org.gethydrated.hydra.core.node;

import java.net.Socket;

/**
 *
 */
public class Connection {

    private final Socket socket;
    private final int id;

    public Connection(Socket socket, int id) {
        this.socket = socket;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getIp() {
        return socket.getInetAddress().toString();
    }

    public int getPort() {
        return socket.getPort();
    }

}
