/**
 * 
 */
package org.gethydrated.hydra.actors.mailbox;

import org.gethydrated.hydra.actors.Reference;

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
     * Target actor.
     */
    private final Reference target;

    /**
     * Source actor.
     */
    private final Reference sender;

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
    public Message(final Object message, final Reference target, final Reference sender) {
        this(message, target, sender, 0);
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
    public Message(final Object message, final Reference target,
            final Reference sender, final Integer id) {
        if (target == null || sender == null) {
            throw new IllegalArgumentException(
                    "Target or sender reference was null");
        }
        this.id = id;
        this.message = message;
        this.sender = sender;
        this.target = target;
    }

    /**
     * 
     * @return target actor.
     */
    public Reference getTarget() {
        return target;
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
