package org.gethydrated.hydra.core.node;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorFactory;
import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.configuration.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class Nodes extends Actor {
    public Nodes(Configuration cfg) {

    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof String) {
            switch ((String) message) {
                case "nodes":
                    nodeList();
            }
        } else if(message instanceof Connection) {
            addNewNode((Connection) message);
        }
    }

    private void addNewNode(final Connection connection) {
        getContext().spawnActor(new ActorFactory() {
            @Override
            public Actor create() throws Exception {
                return new Node(connection);
            }
        }, ""+connection.getId());
    }

    private void nodeList() throws InterruptedException, ExecutionException, TimeoutException {
        List<String> result = new LinkedList<>();
        List<String> nodeList = getContext().getChildren();
        for(String n : nodeList) {
            ActorPath p = getSelf().getPath().createChild(n);
            ActorRef r = getContext().getActor(p);
            Future f = r.ask("information");
            result.add((String) f.get(1, TimeUnit.SECONDS));
        }
        getSender().tell(result, getSelf());
    }

}
