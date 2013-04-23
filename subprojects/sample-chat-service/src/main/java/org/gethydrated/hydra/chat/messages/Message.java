package org.gethydrated.hydra.chat.messages;

import org.gethydrated.hydra.api.service.USID;

/**
 * Chat message.
 */
public class Message {
    private USID usid;
    private String message;

    /**
     * Constructor.
     * @param usid Source usid.
     * @param message Message.
     */
    public Message(final USID usid, final String message) {
        this.usid = usid;
        this.message = message;
    }

    @SuppressWarnings("unused")
    private Message() {
    }

    /**
     * Returns the source usid.
     * @return source usid.
     */
    public USID getUsid() {
        return usid;
    }

    /**
     * Returns the message.
     * @return Message.
     */
    public String getMessage() {
        return message;
    }
}
