package org.gethydrated.hydra.core.internal;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.refs.NullRef;
import org.gethydrated.hydra.api.event.InputEvent;
import org.gethydrated.hydra.api.event.SystemEvent;
import org.gethydrated.hydra.core.cli.CLIResponse;
import org.gethydrated.hydra.core.concurrent.LockRelease;
import org.gethydrated.hydra.core.concurrent.LockReply;
import org.gethydrated.hydra.core.concurrent.LockRequest;
import org.gethydrated.hydra.core.messages.NodeDown;
import org.gethydrated.hydra.core.messages.NodeUp;
import org.gethydrated.hydra.core.registry.RegistryState;
import org.gethydrated.hydra.core.sid.DefaultSIDFactory;
import org.gethydrated.hydra.core.sid.IdMatcher;
import org.gethydrated.hydra.core.transport.*;
import org.gethydrated.hydra.core.transport.RelayRef.RelayedMessage;
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
                    if(!connection.isHidden()) {
                        getSender().tell(connection.getConnector(), getSelf());
                    } else {
                        getSender().tell(null, getSelf());
                    }
                    break;
                case "disconnected":
                    logger.info("Node disconnected: ", getSelf().toString());
                    idMatcher.remove(connection.getUUID());
                    getContext().stopActor(getSelf());
                    break;
            }
        } else if (message instanceof SystemEvent) {
            Envelope env = makeSystemEnvelope(message);
            connection.sendEnvelope(env);
        } else if (message instanceof RelayedMessage) {
            handleRelayed((RelayedMessage)message);
        } else if (message instanceof IOException) {
            logger.warn("Error while reading socket input", (IOException)message);
        } else if (message instanceof Envelope) {
            if(((Envelope) message).getType() == MessageType.SYSTEM) {
                handleSystemEnvelope((Envelope) message);
            }
        } else {
            logger.debug("discarded message: {}", message.getClass().getName());
        }
    }

    private void handleRelayed(RelayedMessage message) {
        System.out.println(message);
        if(message.isSystemRelay()) {
            try {
                Envelope env = makeSystemEnvelope(message.getMessage());
                env.getSObject().setTarget(message.getRelay());
                connection.sendEnvelope(env);
            } catch (IOException e) {
                logger.warn("{}", e.getMessage(), e);
            }
        }

    }

    @Override
    public void onStart() {
        setCallback();
        if(!connection.isHidden()) {
            getSystem().getEventStream().publish(new NodeUp(connection.getUUID()));
        }
    }

    @Override
    public void onStop() throws IOException {
        connection.disconnect();
        if(!connection.isHidden()) {
            getSystem().getEventStream().publish(new NodeDown(connection.getUUID()));
        }
    }

    private void setCallback() {
        connection.setReceiveCallback(getSelf(),getSystem().getDefaultDispatcher().getExecutor());
    }

    private void handleSystemEnvelope(Envelope envelope) throws ClassNotFoundException, IOException {
        getSystem().getClock().sync(envelope.getTimestamp());
        SerializedObject so = envelope.getSObject();
        Class<?> clazz = getClass().getClassLoader().loadClass(so.getClassName());
        Object o = connection.getMapper().readValue(so.getData(), clazz);
        System.out.println(so.getTarget() + " from " + so.getSender());
        if(o instanceof InputEvent) {
            InputEvent i = (InputEvent)o;
            i.setSource(getSelf().toString());
            getSystem().getEventStream().publish(i);
        } else {
            System.out.println("sender  " + so.getSender());
            ActorPath target = DefaultSIDFactory.usidToActorPath(so.getTarget());
            if(target != null) {
                try {
                    ActorRef ref = getContext().getActor(target);
                    if(so.getSender() == null || so.getSender().getTypeId() != 2) {
                        ref.tell(o, getSelf());
                    } else {
                        //The sender usid tells us, that the source of this request is a temp actor.
                        //Install a relay temp actor that will notify us, if an answer has been send.
                        //This is part of the ask functionality between nodes.
                        ActorRef relay = new RelayRef(getSelf(), so.getSender(), true);
                        ref.tell(o, relay);
                    }
                } catch (Exception e ) {
                    logger.warn("An error occurred while processing received system message: {}", e.getMessage(), e);
                }
            } else {
                logger.debug("discarded received message: {}", o.getClass().getName());
            }

        }
    }

    private ActorRef getRecipient(Object o) {
        if(o instanceof CLIResponse) {
            return getContext().getActor("/app/cli");
        }
        if(o instanceof LockRelease || o instanceof LockRequest || o instanceof LockReply) {
            return getContext().getActor("/app/locking");
        }
        if(o instanceof RegistryState) {
            return getContext().getActor("/app/globalregistry");
        }
        return new NullRef();
    }

    private Envelope makeSystemEnvelope(Object systemEvent) throws IOException {
        Envelope env = new Envelope(MessageType.SYSTEM);
        env.setTarget(connection.getUUID());
        env.setSender(idMatcher.getLocal());
        env.setTimestamp(getSystem().getClock().getCurrentTime());
        SerializedObject so = new SerializedObject();
        so.setClassName(systemEvent.getClass().getName());
        so.setFormat("json");
        so.setData(connection.getMapper().writeValueAsBytes(systemEvent));
        so.setSender(DefaultSIDFactory.actorPathToUSID(getSender().getPath(), connection.getUUID()));
        so.setTarget(DefaultSIDFactory.actorPathToUSID(getRecipient(systemEvent).getPath(), connection.getUUID()));
                env.setSObject(so);
        return env;
    }
}
