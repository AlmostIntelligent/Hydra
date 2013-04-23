package org.gethydrated.hydra.core.sid;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.service.SID;

/**
 * Internal service identifier.
 */
public interface InternalSID extends SID {
    
    /**
     * Returns the underlying actor ref.
     * @return actor ref.
     */
    ActorRef getRef();

}
