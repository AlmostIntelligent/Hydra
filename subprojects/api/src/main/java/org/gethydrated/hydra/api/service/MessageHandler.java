package org.gethydrated.hydra.api.service;

/**
 * Message handler interface.
 * 
 * @param <V> Result type.
 * @author Christian Kulpa
 * @author Hanno Sternberg
 * @since 0.2.0
 */
public interface MessageHandler<V> {

    /**
     * Handles the incoming message.
     * @param message Message.
     * @param sender Message source.
     */
    void handle(V message, SID sender);

}
