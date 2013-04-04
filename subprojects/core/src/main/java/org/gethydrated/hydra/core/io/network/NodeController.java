package org.gethydrated.hydra.core.io.network;

import org.gethydrated.hydra.core.transport.Con;
import org.gethydrated.hydra.core.transport.Connection;
import org.gethydrated.hydra.core.transport.NodeAddress;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NodeController {
    private final UUID local = UUID.randomUUID();

    private final Map<UUID, Con> nodes = new HashMap<>();

    public UUID getLocal() {
        return local;
    }

    public int getID(UUID nodeid) {
        if (nodeid.equals(local)) {
            return 0;
        }
        if (nodes.containsKey(nodeid)) {

        }
        return -1;
    }

    public boolean isKnownNode(UUID nodeid) {
        return nodeid.equals(local) || nodes.containsKey(nodeid);
    }

    public NodeAddress getConnector() {
        return null;
    }

    public void addNode(final UUID sender, final Con con) {
        if(!isKnownNode(sender)) {
            System.out.println("new con: " + sender);
            nodes.put(sender, con);
        }
    }
}
