package org.gethydrated.hydra.core.messages;

import java.util.UUID;

/**
 *
 */
public class NodeUp {
    private final UUID uuid;

    public NodeUp(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }
}
