package org.gethydrated.hydra.core.sid;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 */
public class IdMatcher {

    Map<Long, UUID> nodeIds = new HashMap<>();

    long nextId = 0;

    public synchronized void setLocal(UUID localId) {
        if(nodeIds.containsKey(0)) {
            throw new IllegalStateException("Local node already set.");
        }
        nodeIds.put(0L, localId);
    }

    public UUID getLocal() {
        return nodeIds.get(0L);
    }

    public synchronized long addUUID(UUID nodeId) {
        nodeIds.put(++nextId, nodeId);
        return nextId;
    }

    public synchronized UUID getID(long localId) {
        return nodeIds.get(localId);
    }
}
