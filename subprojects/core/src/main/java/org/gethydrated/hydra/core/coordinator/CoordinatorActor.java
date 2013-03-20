package org.gethydrated.hydra.core.coordinator;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.api.configuration.Configuration;

/**
 * 
 * @author Christian Kulpa
 *
 */
public class CoordinatorActor extends Actor {

    private int nodeid = 2;

    private int ownId = 1;

    public CoordinatorActor(Configuration cfg) {

    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof String) {
            switch ((String ) message) {
                case "newNodeId":
                    getSender().tell(nodeid++,getSelf());
                    break;
                case "ownId":
                    getSender().tell(ownId, getSelf());
                    break;
            }
        }
    }
}
