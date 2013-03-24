package org.gethydrated.hydra.core.internal;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorFactory;
import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.core.sid.IdMatcher;
import org.gethydrated.hydra.core.transport.Connection;
import org.gethydrated.hydra.core.transport.NodeAddress;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class Nodes extends Actor {

    private final IdMatcher idMatcher;

    public Nodes(IdMatcher idMatcher) {
        this.idMatcher = idMatcher;
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
        List<String> nodeList = getContext().getChildren();
        for(String n : nodeList) {
            UUID uuid = idMatcher.getUUID(Integer.parseInt(n));
            ActorPath p = getSelf().getPath().createChild(n);
            ActorRef r = getContext().getActor(p);
            Future f = r.ask("connector");
            NodeAddress addr = (NodeAddress) f.get(1, TimeUnit.SECONDS);
            result.put(uuid, addr);
        }
        getSender().tell(result, getSelf());

    }

    private void addNewNode(final Connection connection) {
        getContext().spawnActor(new ActorFactory() {
            @Override
            public Actor create() throws Exception {
                return new Node(connection, idMatcher);
            }
        }, ""+idMatcher.getId(connection.getUUID()));
    }
}
