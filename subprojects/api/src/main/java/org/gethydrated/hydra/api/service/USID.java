package org.gethydrated.hydra.api.service;

import java.io.Serializable;
import java.util.UUID;

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
    /*
    public static USID parse(String sid) {
        if(sid == null) {
            throw new RuntimeException("Sid string cannot be null");
        }
        if(!sid.matches("<[0-9]*:[01]:[0-9]*>")) {
            throw new RuntimeException("Invalid sid string.");
        }
        sid = sid.substring(1,sid.length()-1);
        String[] arr = sid.split(":");
        return new USID(UUID.fromString(arr[0]),Integer.parseInt(arr[1]),Long.parseLong(arr[2]));
    } */
}
