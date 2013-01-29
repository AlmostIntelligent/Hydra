package org.gethydrated.hydra.api.service;

import java.io.Serializable;

/**
 * Unique service identifier.
 *
 * A service is identified by the node he is running on,
 * the type and its id.
 *
 * A node id of 0 is always pointing at the local node.
 * Possible type ids are 1 for hydra internal services and
 * 0 for user services
 *
 */
public class USID implements Serializable {

    public final long nodeId;

    public final int typeId;

    public final long serviceId;

    public USID(Long nodeId, int typeId, long serviceId) {
        this.nodeId = nodeId;
        this.typeId = typeId;
        this.serviceId = serviceId;
    }

    public String toString(){
        return "<"+nodeId+":"+typeId+":"+serviceId+">";
    }
}
