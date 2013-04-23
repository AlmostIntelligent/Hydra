package org.gethydrated.hydra.core.internal;

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
import org.gethydrated.hydra.core.io.network.Connection;
import org.gethydrated.hydra.core.io.network.NodeController;
import org.gethydrated.hydra.core.io.transport.Envelope;
import org.gethydrated.hydra.core.io.transport.MessageType;
import org.gethydrated.hydra.core.io.transport.RelayRef;
import org.gethydrated.hydra.core.io.transport.RelayRef.RelayedMessage;
import org.gethydrated.hydra.core.io.transport.SerializedObject;
import org.gethydrated.hydra.core.registry.RegistryState;
import org.gethydrated.hydra.core.registry.Sync;
import org.gethydrated.hydra.core.service.StopService;
import org.gethydrated.hydra.core.sid.DefaultSIDFactory;
import org.gethydrated.hydra.core.sid.InternalSID;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * Node actor for io between nodes.
 */
public class Node extends Actor {

    private final Logger logger = getLogger(Node.class);
    private final Connection connection;
    private final NodeController nodeController;
    private final DefaultSIDFactory sidFactory;

    /**
     * Constructor.
     * @param connection node connection.
     * @param nodeController node controller.
     * @param sidFactory service id factory.
     */
    public Node(final Connection connection,
            final NodeController nodeController,
            final DefaultSIDFactory sidFactory) {

        this.connection = connection;
        this.nodeController = nodeController;
        this.sidFactory = sidFactory;
        connection.addCloseListener(new Runnable() {
            @Override
            public void run() {
                getContext().stopActor(getSelf());
            }
        });
    }

    @Override
    public void onReceive(final Object message) throws Exception {
        try {
            if (message instanceof SystemEvent) {
                final Envelope env = makeSystemEnvelope(message);
                connection.send(env);
            } else if (message instanceof SerializedObject) {
                final Envelope env = makeEnvelope((SerializedObject) message);
                connection.send(env);
            } else if (message instanceof RelayedMessage) {
                handleRelayed((RelayedMessage) message);
            } else if (message instanceof Envelope) {
                if (((Envelope) message).getType() == MessageType.SYSTEM) {
                    handleSystemEnvelope((Envelope) message);
                } else if (((Envelope) message).getType() == MessageType.USER) {
                    try {
                        final SerializedObject so = ((Envelope) message)
                                .getSObject();
                        final SID sid = sidFactory.fromUSID(so.getTarget());
                        if (((InternalSID) sid).getRef().equals(getSelf())) {
                            throw new RuntimeException("send to self");
                        }
                        sid.tell(so, sidFactory.fromUSID(so.getSender()));
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                logger.debug("discarded message: {}", message.getClass()
                        .getName());
            }
        } catch (final Exception t) {
            logger.error("Error in node actor: {}", getSelf().getName(), t);
        }
    }

    private void handleRelayed(final RelayedMessage message) {
        if (message.isSystemRelay()) {
            try {
                final Envelope env = makeSystemEnvelope(message.getMessage());
                env.getSObject().setTarget(message.getRelay());
                connection.send(env);
            } catch (final IOException e) {
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
        if (connection.channel() != null) {
            Envelope env = new Envelope(MessageType.DISCONNECT);
            env.setTarget(connection.uuid());
            env.setSender(nodeController.getLocal());
            connection.send(env);
            connection.channel().close();
        }
        getSystem().getEventStream().publish(new NodeDown(connection.uuid()));
    }

    private void handleSystemEnvelope(final Envelope envelope)
            throws ClassNotFoundException, IOException {
        getSystem().getClock().sync(envelope.getTimestamp());
        final SerializedObject so = envelope.getSObject();
        final Class<?> clazz = getClass().getClassLoader().loadClass(
                so.getClassName());
        final Object o = nodeController.defaultMapper().readValue(so.getData(),
                clazz);
        if (o instanceof InputEvent) {
            final InputEvent i = (InputEvent) o;
            i.setSource(getSelf().toString());
            getSystem().getEventStream().publish(i);
        } else {
            final ActorPath target = DefaultSIDFactory.usidToActorPath(so
                    .getTarget());
            if (target != null) {
                try {
                    final ActorRef ref = getContext().getActor(target);
                    if (so.getSender() == null
                            || so.getSender().getTypeId() != 2) {
                        ref.tell(o, getSelf());
                    } else {
                        // The sender usid tells us, that the source of this
                        // request is a temp actor.
                        // Install a relay temp actor that will notify us, if an
                        // answer has been send.
                        // This is part of the ask functionality between nodes.
                        final ActorRef relay = new RelayRef(getSelf(),
                                so.getSender(), true);
                        ref.tell(o, relay);
                    }
                } catch (final Exception e) {
                    logger.warn(
                            "An error occurred while processing received system message: {}",
                            e.getMessage(), e);
                }
            } else {
                logger.debug("discarded received message: {}", o.getClass()
                        .getName());
            }

        }
    }

    private ActorRef getRecipient(final Object o) {
        if (o instanceof CLIResponse) {
            return getContext().getActor("/app/cli");
        }
        if (o instanceof LockRelease || o instanceof LockRequest
                || o instanceof LockReply) {
            return getContext().getActor("/app/locking");
        }
        if (o instanceof RegistryState || o instanceof Sync) {
            return getContext().getActor("/app/globalregistry");
        }
        if (o instanceof StopService) {
            return getContext().getActor("/app/services");
        }
        return new NullRef();
    }

    private Envelope makeSystemEnvelope(final Object systemEvent)
            throws IOException {
        final Envelope env = new Envelope(MessageType.SYSTEM);
        env.setTarget(connection.uuid());
        env.setSender(nodeController.getLocal());
        env.setTimestamp(getSystem().getClock().getCurrentTime());
        final SerializedObject so = new SerializedObject();
        so.setClassName(systemEvent.getClass().getName());
        so.setFormat("json");
        so.setData(nodeController.defaultMapper()
                .writeValueAsBytes(systemEvent));
        if (getSender() != null) {
            so.setSender(DefaultSIDFactory.actorPathToUSID(getSender()
                    .getPath(), connection.uuid()));
        }
        if (systemEvent instanceof Monitor) {
            so.setSender(((Monitor) systemEvent).getSource());
            so.setTarget(((Monitor) systemEvent).getTarget());
        } else if (systemEvent instanceof ServiceDown) {
            so.setSender(((ServiceDown) systemEvent).getSource());
            so.setTarget(((ServiceDown) systemEvent).getTarget());
        } else if (systemEvent instanceof UnMonitor
                || systemEvent instanceof Link || systemEvent instanceof Unlink
                || systemEvent instanceof ServiceExit) {
            so.setTarget(((USIDAware) systemEvent).getUSID());
        } else {
            so.setTarget(DefaultSIDFactory.actorPathToUSID(
                    getRecipient(systemEvent).getPath(), connection.uuid()));
        }
        env.setSObject(so);
        if (systemEvent instanceof ServiceDown) {
            System.out.println(env);
        }
        return env;
    }

    private Envelope makeEnvelope(final SerializedObject so) {
        final Envelope env = new Envelope(MessageType.USER);
        env.setTarget(connection.uuid());
        env.setSender(nodeController.getLocal());
        env.setTimestamp(getSystem().getClock().getCurrentTime());
        env.setSObject(so);
        return env;
    }
}
