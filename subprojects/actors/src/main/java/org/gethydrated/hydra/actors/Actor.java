package org.gethydrated.hydra.actors;

import org.gethydrated.hydra.actors.logging.LoggingAdapter;
import org.gethydrated.hydra.actors.node.ActorNode;
import org.slf4j.Logger;

/**
 * Actor interface.
 * 
 * @author Christian Kulpa
 */
public abstract class Actor {

    /**
     * Surrounding actor node.
     */
    private final ActorNode node;

    /**
     * Constructor. Will fail if not called within an actor node.
     */
    public Actor() {
        final ActorNode n = ActorNode.getLocalActorNode();
        if (n == null) {
            throw new IllegalStateException();
        }
        this.node = n;
    }

    /**
     * Message callback. Invoked on messages received.
     * 
     * @param message
     *            Message object.#
     * @throws Exception
     *             on failure.
     */
    public abstract void onReceive(Object message) throws Exception;

    /**
     * Startup callback. Gets called before messages processing starts.
     * 
     * @throws Exception
     *             on failure.
     */
    public void onStart() throws Exception {
    }

    /**
     * Shutdown callback. Gets called after messages processing stops.
     * 
     * @throws Exception
     *             on failure.
     */
    public void onStop() throws Exception {
    }

    /**
     * Returns the actor system this actor lives in.
     * 
     * @return actor system.
     */
    public final ActorSystem getSystem() {
        return node.getSystem();
    }

    /**
     * Creates a new ActorRef that points to this actor.
     * 
     * @return new ActorRef.
     */
    public final ActorRef getSelf() {
        return node.getSelf();
    }

    /**
     * Returns the sender of the current message.
     * 
     * @return Sender actor ref.
     */
    public final ActorRef getSender() {
        return node.getSender();
    }

    /**
     * Returns the actors context.
     * 
     * @return ActorContext.
     */
    public final ActorContext getContext() {
        return node;
    }

    /**
     * Creates an logger that will push its log messages through the actor
     * system.
     * 
     * @param clazz
     *            logger name.
     * @return logger instance.
     */
    public final Logger getLogger(final Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    /**
     * Creates an logger that will push its log messages through the actor
     * system.
     * 
     * @param name
     *            logger name.
     * @return logger instance.
     */
    public final Logger getLogger(final String name) {
        return new LoggingAdapter(name, getSystem());
    }

}
