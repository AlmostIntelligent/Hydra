package org.gethydrated.hydra.core.io.transport;

/**
 * MessageType.
 */
public enum MessageType {
    /**
     * Connect.
     */
    CONNECT,
    /**
     * Ack message.
     */
    ACK,
    /**
     * Accept message.
     */
    ACCEPT,
    /**
     * Decline message.
     */
    DECLINE,
    /**
     * Disconnect message.
     */
    DISCONNECT,
    /**
     * Node list messages.
     */
    NODES,
    /**
     * System messages.
     */
    SYSTEM,
    /**
     * User messages.
     */
    USER
}
