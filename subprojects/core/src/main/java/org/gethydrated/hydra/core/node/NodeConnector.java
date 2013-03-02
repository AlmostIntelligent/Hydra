package org.gethydrated.hydra.core.node;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.core.messages.ConnectTo;

/**
 *
 */
public class NodeConnector extends Actor {

    Configuration cfg;

    public NodeConnector(Configuration cfg) {
        this.cfg = cfg;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof String) {
            switch ((String)message) {
                case "portchanged":
                    setChangedPort();
            }
        } else if(message instanceof ConnectTo) {
            connectNode((ConnectTo) message);
        }
    }

    private void connectNode(ConnectTo target) {
        getLogger(NodeConnector.class).info("connecting to: " + target.ip + ":" + target.port);
    }

    private void setChangedPort() {
        getLogger(NodeConnector.class).info("Connector port changed.");
    }

}
