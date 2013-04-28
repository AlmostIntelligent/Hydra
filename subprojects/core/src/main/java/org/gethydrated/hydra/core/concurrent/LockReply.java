package org.gethydrated.hydra.core.concurrent;

import java.util.UUID;

import org.gethydrated.hydra.api.event.SystemEvent;

/**
 * LockReply message.
 */
public class LockReply implements SystemEvent {

    private long timestamp;

    private UUID nodeId;

    /**
     * Constructor.
     * @param nodeId node uuid.
     * @param timestamp logical clock timestamp.
     */
    public LockReply(final UUID nodeId, final long timestamp) {
        this.timestamp = timestamp;
        this.nodeId = nodeId;
    }

    @SuppressWarnings("unused")
    private LockReply() {
    }

    /**
     * Returns the reply timestamp.
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
    public String toString() {
        return "LockReply{" + "timestamp=" + timestamp + ", nodeId=" + nodeId
                + '}';
    }
}
