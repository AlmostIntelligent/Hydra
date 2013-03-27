package org.gethydrated.hydra.core.concurrent;

import org.gethydrated.hydra.api.event.SystemEvent;

import java.util.UUID;

/**
 *
 */
public class LockReply implements SystemEvent {

    private long timestamp;

    private UUID nodeId;

    public LockReply(UUID nodeId, long timestamp) {
        this.timestamp = timestamp;
        this.nodeId = nodeId;
    }

    private LockReply() {}

    public long getTimestamp() {
        return timestamp;
    }

    public UUID getNodeId() {
        return nodeId;
    }
}
