package org.gethydrated.hydra.core.internal;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.core.transport.Connection;

/**
 *
 */
public class Node extends Actor {

    private final Connection connection;

    public Node(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof String) {
            switch ((String) message) {
                case "connector":
                    getSender().tell(connection.getConnector(), getSelf());
            }
        }
    }

    @Override
    public void onStart() {
        connection.setReceiveCallback(getSelf());
    }
}
