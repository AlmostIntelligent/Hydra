package org.gethydrated.hydra.api.service;

import java.io.Serializable;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Unique service identifier.
 * 
 * A service is identified by the node he is running on, the type and its id.
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class USID implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 414478715605451372L;

    private UUID nodeId;

    private int typeId;

    private long serviceId;

    /**
     * Construction.
     * @param nodeId node uuid.
     * @param typeId type id.
     * @param serviceId service id.
     */
    public USID(final UUID nodeId, final int typeId, final long serviceId) {
        this.nodeId = nodeId;
        this.typeId = typeId;
        this.serviceId = serviceId;
    }

    @SuppressWarnings("unused")
    private USID() {
    }

    /**
     * Returns the node uuid.
     * @return node uuid.
     */
    public UUID getNodeId() {
        return nodeId;
    }

    /**
     * Returns the type id.
     * @return type id.
     */
    public int getTypeId() {
        return typeId;
    }

    /**
     * Returns the service id.
     * @return service id.
     */
    public long getServiceId() {
        return serviceId;
    }

    @Override
    public String toString() {
        return "<" + nodeId + ":" + typeId + ":" + serviceId + ">";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final USID usid = (USID) o;

        if (serviceId != usid.serviceId) {
            return false;
        }
        if (typeId != usid.typeId) {
            return false;
        }
        if (nodeId != null ? !nodeId.equals(usid.nodeId) : usid.nodeId != null) {
            return false;
        }

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
