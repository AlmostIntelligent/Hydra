package org.gethydrated.hydra.core.concurrent;

import java.util.UUID;

import org.gethydrated.hydra.api.event.SystemEvent;

/**
 * LockRequest message.
 */
public class LockRequest implements SystemEvent {

    private long timestamp;

    private UUID nodeId;

    /**
     * Constructor.
     * @param nodeId node uuid.
     * @param timestamp logical clock timestamp.
     */
    public LockRequest(final UUID nodeId, final long timestamp) {
        this.timestamp = timestamp;
        this.nodeId = nodeId;
    }

    @SuppressWarnings("unused")
    private LockRequest() {
    }

    /**
     * Returns the request timestamp.
     * @return timestamp.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the node uuid.
     * @return node uuid.
     */
    public UUID getNodeId() {
        return nodeId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final LockRequest that = (LockRequest) o;

        if (nodeId != null ? !nodeId.equals(that.nodeId) : that.nodeId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return nodeId != null ? nodeId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "LockRequest{" + "timestamp=" + timestamp + ", nodeId=" + nodeId
                + '}';
    }
}
