package org.gethydrated.hydra.api.event;

/**
 * Dead message of a stopped actor.
 */
public class DeadMessage {
    private final Object message;

    public DeadMessage(Object message) {
        this.message = message;
    }

    public Object getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "DeadMessage{" +
                "message=" + message +
                '}';
    }
}
