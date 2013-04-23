package org.gethydrated.hydra.api.event;

import java.util.UUID;

/**
 * NodeDown event.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 */
public class NodeDown {
    private final UUID uuid;

    /**
     * Constructor.
     * @param uuid node uuid.
     */
    public NodeDown(final UUID uuid) {
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
