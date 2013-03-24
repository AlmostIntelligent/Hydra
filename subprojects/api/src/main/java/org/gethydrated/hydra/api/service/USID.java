package org.gethydrated.hydra.api.service;

import java.io.Serializable;
import java.util.UUID;

/**
 * Unique service identifier.
 *
 * A service is identified by the node he is running on,
 * the type and its id.
 *
 */
public class USID implements Serializable {

    public final UUID nodeId;

    public final int typeId;

    public final long serviceId;

    public USID(UUID nodeId, int typeId, long serviceId) {
        this.nodeId = nodeId;
        this.typeId = typeId;
        this.serviceId = serviceId;
    }

    public String toString(){
        return "<"+nodeId+":"+typeId+":"+serviceId+">";
    }
}
