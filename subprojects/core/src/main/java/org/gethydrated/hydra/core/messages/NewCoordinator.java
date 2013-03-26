package org.gethydrated.hydra.core.messages;

import org.gethydrated.hydra.api.event.SystemEvent;

import java.util.UUID;

/**
 *
 */
public class NewCoordinator implements SystemEvent {
    private UUID nodeId;

    public NewCoordinator(UUID local) {
        nodeId = local;
    }

    private NewCoordinator() {}

    public UUID getNodeId() {
        return nodeId;
    }
}
