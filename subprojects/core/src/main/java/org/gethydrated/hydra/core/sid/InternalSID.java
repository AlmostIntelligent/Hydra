package org.gethydrated.hydra.core.sid;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.service.SID;

/**
 *
 */
public interface InternalSID extends SID {

    ActorRef getRef();

}
