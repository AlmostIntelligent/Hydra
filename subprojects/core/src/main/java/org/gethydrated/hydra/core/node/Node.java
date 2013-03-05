package org.gethydrated.hydra.core.node;

import org.gethydrated.hydra.actors.Actor;

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
                case "information":
                    getSender().tell("Node " + connection.getId() +": "+connection.getIp()+":"+connection.getPort(), getSelf());
            }
        }
    }
}
