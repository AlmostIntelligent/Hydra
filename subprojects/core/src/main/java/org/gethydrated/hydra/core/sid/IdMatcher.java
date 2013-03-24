package org.gethydrated.hydra.core.sid;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.UUID;

/**
 *
 */
public class IdMatcher {

    private final BiMap<Integer, UUID> nodeIds = HashBiMap.create();

    private int nextId = 0;

    public synchronized void setLocal(UUID localId) {
        if(nodeIds.containsKey(0)) {
            throw new IllegalStateException("Local node already set.");
        }
        nodeIds.put(0, localId);
    }

    public synchronized UUID getLocal() {
        return nodeIds.get(0);
    }

    public synchronized int addUUID(UUID nodeId) {
        nodeIds.put(++nextId, nodeId);
        return nextId;
    }

    public synchronized UUID getUUID(int nodeId) {
        return nodeIds.get(nodeId);
    }

    public synchronized int getId(UUID nodeUUID) {
        return nodeIds.inverse().get(nodeUUID);
    }

    public synchronized boolean contains(UUID nodeUUID) {
        return nodeIds.containsValue(nodeUUID);
    }
}
