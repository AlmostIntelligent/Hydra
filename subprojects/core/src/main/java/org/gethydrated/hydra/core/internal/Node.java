package org.gethydrated.hydra.core.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.api.event.SystemEvent;
import org.gethydrated.hydra.core.sid.IdMatcher;
import org.gethydrated.hydra.core.transport.Connection;
import org.slf4j.Logger;

import java.io.IOException;

/**
 *
 */
public class Node extends Actor {

    private final Connection connection;

    private final Logger logger = getLogger(Node.class);
    private final IdMatcher idMatcher;

    public Node(Connection connection, IdMatcher idMatcher) {
        this.connection = connection;
        this.idMatcher = idMatcher;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof String) {
            switch ((String) message) {
                case "connector":
                    getSender().tell(connection.getConnector(), getSelf());
                    break;
                case "disconnected":
                    logger.info("Node disconnected: ", getSelf().toString());
                    idMatcher.remove(connection.getUUID());
                    getContext().stop(getSelf());
                    break;
            }
        } else if (message instanceof SystemEvent) {
            sendSystemEnvelope(message);
        } else if (message instanceof IOException) {
            logger.warn("Error while reading socket input", (IOException)message);
            if(connection.isClosed()) {
                System.out.println("cloooooosed");
            } else {
                System.out.println("not cloooosed O__O");
            }
        }
    }

    @Override
    public void onStart() {
        setCallback();
    }

    @Override
    public void onStop() throws IOException {
        connection.disconnect();
    }

    private void setCallback() {
        connection.setReceiveCallback(getSelf(),getSystem().getDefaultDispatcher().getExecutor());
    }

    private void sendSystemEnvelope(Object systemEvent) throws JsonProcessingException {
        logger.debug("Sending system message: " + systemEvent.getClass().getName());
        logger.debug("Message:" + connection.getMapper().writeValueAsString(systemEvent));
    }
}
