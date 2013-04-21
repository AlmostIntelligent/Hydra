package org.gethydrated.hydra.core.internal;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorFactory;
import org.gethydrated.hydra.core.io.network.Connection;
import org.gethydrated.hydra.core.io.network.NodeController;
import org.gethydrated.hydra.core.sid.DefaultSIDFactory;

import java.util.HashSet;

/**
 *
 */
public class Nodes extends Actor {

    private final NodeController nodeController;

    private final DefaultSIDFactory sidFactory;

    public Nodes(NodeController nodeController, DefaultSIDFactory sidFactory) {
        this.nodeController = nodeController;
        this.sidFactory = sidFactory;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof String) {
            switch ((String) message) {
                case "nodes":
                    getSender().tell(new HashSet<>(nodeController.getNodes()), getSelf());
                    break;
            }
        } else if (message instanceof Connection) {
            addNewNode((Connection) message);
        }
    }

    private void addNewNode(final Connection connection) {
        getContext().spawnActor( new ActorFactory() {
            @Override
            public Actor create() throws Exception {
                return new Node(connection, nodeController, sidFactory);
            }
        }, String.valueOf(connection.id()));
    }
}
