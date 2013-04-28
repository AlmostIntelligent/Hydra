package org.gethydrated.hydra.api.event;

import java.util.UUID;

/**
 * NodeUp event.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 */
public class NodeUp {
    private final UUID uuid;

    /**
     * Constructor.
     * @param uuid node uuid.
     */
    public NodeUp(final UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Returns the node uuid.
     * @return node uuid.
     */
    public UUID getUuid() {
        return uuid;
    }
}
