package org.gethydrated.hydra.core.concurrent;

import org.gethydrated.hydra.api.event.SystemEvent;

import java.util.UUID;

/**
 *
 */
public class LockRelease implements SystemEvent {

    private UUID nodeId;

    public LockRelease(UUID nodeId) {
        this.nodeId = nodeId;
    }

    private LockRelease() {}

    public UUID getNodeId() {
        return nodeId;
    }

    @Override
    public String toString() {
        return "LockRelease{" +
                "nodeId=" + nodeId +
                '}';
    }
}
