package org.gethydrated.hydra.core.sid;

import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.service.SID;

/**
 * Service id factory.
 */
public interface SIDFactory {

    SID getSIDFromString(String sid);

    SID getSIDFromActorPath(ActorPath sid);

    SID getSIDFromActorRef(ActorRef ref);

}
