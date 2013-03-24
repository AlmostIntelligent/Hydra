package org.gethydrated.hydra.core.internal;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.event.InputEvent;
import org.gethydrated.hydra.api.event.SystemEvent;
import org.gethydrated.hydra.core.cli.CLIResponse;
import org.gethydrated.hydra.core.sid.IdMatcher;
import org.gethydrated.hydra.core.transport.Connection;
import org.gethydrated.hydra.core.transport.Envelope;
import org.gethydrated.hydra.core.transport.MessageType;
import org.gethydrated.hydra.core.transport.SerializedObject;
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
        } else if (message instanceof Envelope) {
            if(((Envelope) message).getType() == MessageType.SYSTEM) {
                handleSystemEnvelope((Envelope) message);
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

    private void handleSystemEnvelope(Envelope envelope) throws ClassNotFoundException, IOException {
        SerializedObject so = envelope.getSObject();
        Class<?> clazz = getClass().getClassLoader().loadClass(so.getClassName());
        Object o = connection.getMapper().readValue(so.getData(), clazz);
        if(o instanceof InputEvent) {
            InputEvent i = (InputEvent)o;
            i.setSource(getSelf().toString());
            getSystem().getEventStream().publish(i);
        } else if(o instanceof CLIResponse) {
            CLIResponse cr = (CLIResponse) o;
            ActorRef ref = getContext().getActor("/app/cli");
            ref.tell(cr, getSelf());
        }
    }

    private void sendSystemEnvelope(Object systemEvent) throws IOException {
        logger.debug("Sending system message: " + systemEvent.getClass().getName());
        logger.debug(systemEvent.toString());
        logger.debug("Message:" + connection.getMapper().writeValueAsString(systemEvent));
        Envelope env = new Envelope(MessageType.SYSTEM);
        env.setTarget(connection.getUUID());
        env.setSender(idMatcher.getLocal());
        SerializedObject so = new SerializedObject();
        so.setClassName(systemEvent.getClass().getName());
        so.setFormat("json");
        so.setData(connection.getMapper().writeValueAsBytes(systemEvent));
        env.setSObject(so);
        connection.sendEnvelope(env);
    }
}
