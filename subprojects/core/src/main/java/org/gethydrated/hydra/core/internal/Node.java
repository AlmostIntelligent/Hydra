package org.gethydrated.hydra.core.internal;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.refs.NullRef;
import org.gethydrated.hydra.api.event.*;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.USIDAware;
import org.gethydrated.hydra.core.cli.CLIResponse;
import org.gethydrated.hydra.core.concurrent.LockRelease;
import org.gethydrated.hydra.core.concurrent.LockReply;
import org.gethydrated.hydra.core.concurrent.LockRequest;
import org.gethydrated.hydra.core.io.network.NodeController;
import org.gethydrated.hydra.core.io.network.Connection;
import org.gethydrated.hydra.core.messages.StopService;
import org.gethydrated.hydra.core.registry.RegistryState;
import org.gethydrated.hydra.core.registry.Sync;
import org.gethydrated.hydra.core.sid.DefaultSIDFactory;
import org.gethydrated.hydra.core.sid.InternalSID;
import org.gethydrated.hydra.core.io.transport.*;
import org.gethydrated.hydra.core.io.transport.RelayRef.RelayedMessage;
import org.slf4j.Logger;

import java.io.IOException;

/**
 *
 */
public class Node extends Actor {

    private final Logger logger = getLogger(Node.class);
    private final Connection connection;
    private final NodeController nodeController;
    private final DefaultSIDFactory sidFactory;

    public Node(Connection connection, NodeController nodeController, DefaultSIDFactory sidFactory) {
        this.connection = connection;
        this.nodeController = nodeController;
        this.sidFactory = sidFactory;
        connection.channel().closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                getContext().stopActor(getSelf());
            }
        });
    }

    @Override
    public void onReceive(Object message) throws Exception {
        try {
        if (message instanceof SystemEvent) {
            Envelope env = makeSystemEnvelope(message);
            connection.send(env);
        } else if (message instanceof SerializedObject) {
            Envelope env = makeEnvelope((SerializedObject) message);
            connection.send(env);
        } else if (message instanceof RelayedMessage) {
            handleRelayed((RelayedMessage) message);
        } else if (message instanceof Envelope) {
            if(((Envelope) message).getType() == MessageType.SYSTEM) {
                handleSystemEnvelope((Envelope) message);
            } else if (((Envelope) message).getType() == MessageType.USER) {
                try {
                    SerializedObject so = ((Envelope) message).getSObject();
                    SID sid = sidFactory.fromUSID(so.getTarget());
                    if (((InternalSID) sid).getRef().equals(getSelf())) {
                        throw new RuntimeException("send to self");
                    }
                    sid.tell(so, sidFactory.fromUSID(so.getSender()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            logger.debug("discarded message: {}", message.getClass().getName());
        }
        }
        catch (Exception t) {
            logger.error("Error in node actor: {}", getSelf().getName(), t);
        }
    }

    private void handleRelayed(RelayedMessage message) {
        if(message.isSystemRelay()) {
            try {
                Envelope env = makeSystemEnvelope(message.getMessage());
                env.getSObject().setTarget(message.getRelay());
                connection.send(env);
            } catch (IOException e) {
                logger.warn("{}", e.getMessage(), e);
            }
        }

    }

    @Override
    public void onStart() {
        getSystem().getEventStream().publish(new NodeUp(connection.uuid()));
    }

    @Override
    public void onStop() throws IOException {
        getSystem().getEventStream().publish(new NodeDown(connection.uuid()));
    }

    private void handleSystemEnvelope(Envelope envelope) throws ClassNotFoundException, IOException {
        getSystem().getClock().sync(envelope.getTimestamp());
        SerializedObject so = envelope.getSObject();
        Class<?> clazz = getClass().getClassLoader().loadClass(so.getClassName());
        Object o = nodeController.defaultMapper().readValue(so.getData(), clazz);
        if(o instanceof InputEvent) {
            InputEvent i = (InputEvent)o;
            i.setSource(getSelf().toString());
            getSystem().getEventStream().publish(i);
        } else {
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
        if(o instanceof RegistryState || o instanceof Sync) {
            return getContext().getActor("/app/globalregistry");
        }
        if(o instanceof StopService) {
            return getContext().getActor("/app/services");
        }
        return new NullRef();
    }

    private Envelope makeSystemEnvelope(Object systemEvent) throws IOException {
        Envelope env = new Envelope(MessageType.SYSTEM);
        env.setTarget(connection.uuid());
        env.setSender(nodeController.getLocal());
        env.setTimestamp(getSystem().getClock().getCurrentTime());
        SerializedObject so = new SerializedObject();
        so.setClassName(systemEvent.getClass().getName());
        so.setFormat("json");
        so.setData(nodeController.defaultMapper().writeValueAsBytes(systemEvent));
        if (getSender() != null) {
            so.setSender(DefaultSIDFactory.actorPathToUSID(getSender().getPath(), connection.uuid()));
        }
        if (systemEvent instanceof Monitor || systemEvent instanceof UnMonitor || systemEvent instanceof Link
                || systemEvent instanceof Unlink || systemEvent instanceof ServiceDown || systemEvent instanceof ServiceExit) {
            so.setTarget(((USIDAware) systemEvent).getUSID());
        } else {
            so.setTarget(DefaultSIDFactory.actorPathToUSID(getRecipient(systemEvent).getPath(), connection.uuid()));
        }
        env.setSObject(so);
        return env;
    }

    private Envelope makeEnvelope(SerializedObject so) {
        Envelope env = new Envelope(MessageType.USER);
        env.setTarget(connection.uuid());
        env.setSender(nodeController.getLocal());
        env.setTimestamp(getSystem().getClock().getCurrentTime());
        env.setSObject(so);
        return env;
    }
}
