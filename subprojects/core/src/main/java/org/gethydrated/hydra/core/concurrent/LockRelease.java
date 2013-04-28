package org.gethydrated.hydra.core.concurrent;

import java.util.UUID;

import org.gethydrated.hydra.api.event.SystemEvent;

/**
 * LockRelease message.
 */
public class LockRelease implements SystemEvent {

    private UUID nodeId;

    /**
     * Constructor.
     * @param nodeId node uuid.
     */
    public LockRelease(final UUID nodeId) {
        this.nodeId = nodeId;
    }

    @SuppressWarnings("unused")
    private LockRelease() {
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
        return "LockRelease{" + "nodeId=" + nodeId + '}';
    }
}
