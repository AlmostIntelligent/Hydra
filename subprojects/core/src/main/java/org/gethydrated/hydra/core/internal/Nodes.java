package org.gethydrated.hydra.core.internal;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorFactory;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.refs.InternalRef;
import org.gethydrated.hydra.api.event.NodeDown;
import org.gethydrated.hydra.core.sid.DefaultSIDFactory;
import org.gethydrated.hydra.core.sid.IdMatcher;
import org.gethydrated.hydra.core.transport.Connection;
import org.gethydrated.hydra.core.transport.NodeAddress;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class Nodes extends Actor {

    private final IdMatcher idMatcher;

    private final DefaultSIDFactory sidFactory;

    private Map<UUID, NodeAddress> nodes;

    private boolean pauseIO = false;

    public Nodes(IdMatcher idMatcher, DefaultSIDFactory sidFactory) {
        this.idMatcher = idMatcher;
        this.sidFactory = sidFactory;
        this.nodes = new HashMap<>();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof String) {
            switch ((String) message) {
                case "nodes":
                    getSender().tell(new HashMap<>(nodes), getSelf());
                    break;
                case "pauseIO":
                    if (!pauseIO) {
                        pauseIO = true;
                        for(ActorRef ref : getContext().getChildren()) {
                            ((InternalRef)ref).suspend();
                        }
                    }
                    break;
                case "resumeIO":
                    if (pauseIO) {
                        pauseIO = false;
                        for(ActorRef ref : getContext().getChildren()) {
                            ((InternalRef)ref).resume(null);
                        }
                    }
                    break;
            }
        } else if (message instanceof Connection) {
            addNewNode((Connection) message);
        } else if (message instanceof NodeDown) {
            nodes.remove(((NodeDown) message).getUuid());
        }
    }

    private void addNewNode(final Connection connection) {
        nodes.put(connection.getUUID(), connection.getConnector());
        InternalRef ref = (InternalRef) getContext().spawnActor(new ActorFactory() {
            @Override
            public Actor create() throws Exception {
                return new Node(connection, idMatcher, sidFactory);
            }
        }, ""+idMatcher.getId(connection.getUUID()));
        if (pauseIO) {
            ref.suspend();
        }
        ref.tell("init", getSelf());
    }
}
