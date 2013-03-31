package org.gethydrated.hydra.core.internal;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorFactory;
import org.gethydrated.hydra.actors.ActorRef;
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

    public Nodes(IdMatcher idMatcher, DefaultSIDFactory sidFactory) {
        this.idMatcher = idMatcher;
        this.sidFactory = sidFactory;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof String) {
            switch ((String) message) {
                case "nodes":
                    nodeMap();
            }
        } else if(message instanceof Connection) {
            addNewNode((Connection) message);
        }
    }

    private void nodeMap() throws InterruptedException, ExecutionException, TimeoutException {
        Map<UUID, NodeAddress> result = new HashMap<>();
        List<ActorRef> nodeList = getContext().getChildren();
        for(ActorRef ref : nodeList) {
            Future f = ref.ask("connector");
            NodeAddress addr = (NodeAddress) f.get(1, TimeUnit.SECONDS);
            if(addr != null) {
                result.put(idMatcher.getUUID(Integer.parseInt(ref.getName())), addr);
            }
        }
        getSender().tell(result, getSelf());

    }

    private void addNewNode(final Connection connection) {
        getContext().spawnActor(new ActorFactory() {
            @Override
            public Actor create() throws Exception {
                return new Node(connection, idMatcher, sidFactory);
            }
        }, ""+idMatcher.getId(connection.getUUID()));
    }
}
