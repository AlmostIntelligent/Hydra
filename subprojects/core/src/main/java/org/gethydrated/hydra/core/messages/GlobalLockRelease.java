package org.gethydrated.hydra.core.messages;

import org.gethydrated.hydra.api.event.SystemEvent;

import java.util.UUID;

/**
 *
 */
public class GlobalLockRelease implements SystemEvent {
    private UUID nodeId;

    public GlobalLockRelease(UUID nodeId) {
        this.nodeId = nodeId;
    }

    private GlobalLockRelease() {}

    public UUID getNodeId() {
        return nodeId;
    }
}
