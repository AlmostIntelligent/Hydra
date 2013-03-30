package org.gethydrated.hydra.api.event;

import java.util.UUID;

/**
 *
 */
public class NodeDown {
    private final UUID uuid;

    public NodeDown(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }
}
