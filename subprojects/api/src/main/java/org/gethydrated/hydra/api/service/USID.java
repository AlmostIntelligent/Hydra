package org.gethydrated.hydra.api.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.util.UUID;

/**
 * Unique service identifier.
 *
 * A service is identified by the node he is running on,
 * the type and its id.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class USID implements Serializable {

    private UUID nodeId;

    private int typeId;

    private long serviceId;

    public USID(UUID nodeId, int typeId, long serviceId) {
        this.nodeId = nodeId;
        this.typeId = typeId;
        this.serviceId = serviceId;
    }

    private USID() {}

    public UUID getNodeId() {
        return nodeId;
    }

    public int getTypeId() {
        return typeId;
    }

    public long getServiceId() {
        return serviceId;
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
