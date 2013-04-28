package org.gethydrated.hydra.core.internal;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorFactory;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.core.io.network.Connection;
import org.gethydrated.hydra.core.io.network.NodeController;
import org.gethydrated.hydra.core.sid.DefaultSIDFactory;

import java.util.HashSet;

/**
 * The Nodes actor is the parent actor of all node
 * actors. 
 */
public class Nodes extends Actor {

    private final NodeController nodeController;

    private final DefaultSIDFactory sidFactory;

    /**
     * Constructor.
     * @param nodeController node controller.
     * @param sidFactory service id factory.
     */
    public Nodes(final NodeController nodeController,
            final DefaultSIDFactory sidFactory) {
        this.nodeController = nodeController;
        this.sidFactory = sidFactory;
    }

    @Override
    public void onReceive(final Object message) throws Exception {
        if (message instanceof String) {
            switch ((String) message) {
            case "nodes":
                getSender().tell(new HashSet<>(nodeController.getNodes()),
                        getSelf());
                break;
            default:
                break;
            }
        } else if (message instanceof Connection) {
            addNewNode((Connection) message);
        }
    }

    private void addNewNode(final Connection connection) {
        ActorRef ref = getContext().spawnActor(new ActorFactory() {
            @Override
            public Actor create() throws Exception {
                return new Node(connection, nodeController, sidFactory);
            }
        }, String.valueOf(connection.id()));
        getSender().tell(ref, getSelf());
    }
}
