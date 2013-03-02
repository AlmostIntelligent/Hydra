package org.gethydrated.hydra.core.messages;

/**
 *
 */
public class ConnectTo {
    public final String ip;

    public final int port;

    public ConnectTo(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}
