package org.gethydrated.hydra.core.node;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.api.configuration.Configuration;

import java.util.LinkedList;
import java.util.List;

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
        }
    }

    private void nodeList() {
        List<String> nodeList = new LinkedList<>();
        getSender().tell(nodeList, getSelf());
    }

}
