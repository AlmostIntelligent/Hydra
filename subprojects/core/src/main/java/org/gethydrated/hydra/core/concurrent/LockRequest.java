package org.gethydrated.hydra.core.concurrent;

import org.gethydrated.hydra.api.event.SystemEvent;

import java.util.UUID;

/**
 *
 */
public class LockRequest implements SystemEvent {

    private long timestamp;

    private UUID nodeId;

    public LockRequest(UUID nodeId, long timestamp) {
        this.timestamp = timestamp;
        this.nodeId = nodeId;
    }

    private LockRequest() {}

    public long getTimestamp() {
        return timestamp;
    }

    public UUID getNodeId() {
        return nodeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LockRequest that = (LockRequest) o;

        if (nodeId != null ? !nodeId.equals(that.nodeId) : that.nodeId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return nodeId != null ? nodeId.hashCode() : 0;
    }
}
