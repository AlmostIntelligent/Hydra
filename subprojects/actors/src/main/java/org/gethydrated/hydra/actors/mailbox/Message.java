/**
 * 
 */
package org.gethydrated.hydra.actors.mailbox;

import java.util.Objects;

import org.gethydrated.hydra.actors.ActorRef;

/**
 * Hydra actor message encapsulation.
 * 
 * @author Christian Kulpa
 * 
 */
public final class Message {
    /**
     * Message object.
     */
    private final Object message;

    /**
     * Source actor.
     */
    private final ActorRef sender;

    /**
     * Message identification id.
     */
    private final Integer id;

    /**
     * Creates a new Message.
     * 
     * @param message
     *            Message object.
     * @param target
     *            Target actor.
     * @param sender
     *            Source actor.
     */
    public Message(final Object message, final ActorRef sender) {
        this(message, sender, 0);
    }

    /**
     * Creates a new Message with a message id for response mapping. The id must
     * not be 0.
     * 
     * @param message
     *            Message object.
     * @param target
     *            Target actor.
     * @param sender
     *            Source actor.
     * @param id
     *            Message id.
     */
    public Message(final Object message, final ActorRef sender, final Integer id) {
        this.id = id;
        this.message = Objects.requireNonNull(message);
        this.sender = sender;
    }

    /**
     * 
     * @return source actor.
     */
    public Object getSender() {
        return sender;
    }

    /**
     * 
     * @return message object.
     */
    public Object getMessage() {
        return message;
    }

    /**
     * 
     * @return message id.
     */
    public Integer getId() {
        return id;
    }
}
