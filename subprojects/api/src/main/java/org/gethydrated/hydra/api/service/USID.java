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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        USID usid = (USID) o;

        if (serviceId != usid.serviceId) return false;
        if (typeId != usid.typeId) return false;
        if (nodeId != null ? !nodeId.equals(usid.nodeId) : usid.nodeId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nodeId != null ? nodeId.hashCode() : 0;
        result = 31 * result + typeId;
        result = 31 * result + (int) (serviceId ^ (serviceId >>> 32));
        return result;
    }
}
