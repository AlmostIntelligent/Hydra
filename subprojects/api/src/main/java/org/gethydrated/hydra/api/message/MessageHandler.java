package org.gethydrated.hydra.api.message;

/**
 * Message handler interface.
 * @author Christian Kulpa
 *
 */
public interface MessageHandler {
    
    /**
     * Processes a messages.
     * @param m Message.
     */
    void processMessage(Message m);
}
