/**
 * 
 */
package org.gethydrated.hydra.actors.mailbox;

import org.gethydrated.hydra.actors.ActorRef;

import java.util.Objects;

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
     * Creates a new Message.
     * 
     * @param message
     *            Message object.
     * @param sender
     *            Source actor.
     */
    public Message(final Object message, final ActorRef sender) {
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
}
