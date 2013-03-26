package org.gethydrated.hydra.core.messages;

import org.gethydrated.hydra.api.event.SystemEvent;

import java.util.UUID;

/**
 *
 */
public class GlobalLockRequest implements SystemEvent {
    private UUID nodeId;

    public GlobalLockRequest(UUID nodeId) {
        this.nodeId = nodeId;
    }

    private GlobalLockRequest() {}

    public UUID getNodeId() {
        return nodeId;
    }
}
