package org.gethydrated.hydra.core.internal;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.event.InputEvent;
import org.gethydrated.hydra.api.event.SystemEvent;
import org.gethydrated.hydra.core.cli.CLIResponse;
import org.gethydrated.hydra.core.concurrent.LockRelease;
import org.gethydrated.hydra.core.concurrent.LockReply;
import org.gethydrated.hydra.core.concurrent.LockRequest;
import org.gethydrated.hydra.core.messages.NodeDown;
import org.gethydrated.hydra.core.messages.NodeUp;
import org.gethydrated.hydra.core.sid.IdMatcher;
import org.gethydrated.hydra.core.transport.Connection;
import org.gethydrated.hydra.core.transport.Envelope;
import org.gethydrated.hydra.core.transport.MessageType;
import org.gethydrated.hydra.core.transport.SerializedObject;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.*;

/**
 *
 */
public class Node extends Actor {

    private final Connection connection;

    private final Logger logger = getLogger(Node.class);
    private final IdMatcher idMatcher;

    private final ConcurrentMap<Integer, ActorRef> futureMap = new ConcurrentHashMap<>();
    private final Random random = new Random();

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
                    getContext().stop(getSelf());
                    break;
            }
        } else if (message instanceof SystemEvent) {
            Envelope env = makeSystemEnvelope(message);
            connection.sendEnvelope(env);
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
        if(o instanceof InputEvent) {
            InputEvent i = (InputEvent)o;
            i.setSource(getSelf().toString());
            getSystem().getEventStream().publish(i);
        } else {
            ActorRef ref = getRecipient(o);
            if(ref != null) {
                if(envelope.isFuture()) {
                    Future f = ref.ask(o);
                    Object res;
                    try {
                        res = f.get(10, TimeUnit.SECONDS);
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        res = e;
                    }
                    Envelope env = makeSystemEnvelope(res);
                    env.setFutureResult(true);
                    env.setFutureId(envelope.getFutureId());
                    connection.sendEnvelope(env);
                } else if (envelope.isFutureResult()) {
                    ref = futureMap.remove(envelope.getFutureId());
                    if(ref != null) {
                        ref.tell(o, getSelf());
                    }
                } else {
                    ref.tell(o, getSelf());
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
        return null;
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
        env.setSObject(so);
        if(getSender() != null &&getSender().getName().equals("future")) {
            Integer id = random.nextInt();
            while (futureMap.containsKey(id)) {
                id = random.nextInt();
            }
            futureMap.put(id, getSender());
            env.setFuture(true);
            env.setFutureId(id);
        }
        return env;
    }
}
