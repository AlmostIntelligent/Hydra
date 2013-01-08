/**
 * 
 */
package org.gethydrated.hydra.actors.mailbox;

import java.util.Objects;

import org.gethydrated.hydra.actors.ActorRef;

/**
 * Hydra actor messages encapsulation.
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
     * @param sender
     *            Source actor.
     */
    public Message(final Object message, final ActorRef sender) {
        this(message, sender, 0);
    }

    /**
     * Creates a new Message with a messages id for response mapping. The id must
     * not be 0.
     * 
     * @param message
     *            Message object.
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
    public ActorRef getSender() {
        return sender;
    }

    /**
     * 
     * @return messages object.
     */
    public Object getMessage() {
        return message;
    }

    /**
     * 
     * @return messages id.
     */
    public Integer getId() {
        return id;
    }
}
