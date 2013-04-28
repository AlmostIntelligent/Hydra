package org.gethydrated.hydra.actors;

import java.util.List;

/**
 *
 */
public interface ActorContext extends ActorSource {

    ActorRef getSelf();

    ActorRef getSender();

    ActorSystem getSystem();

    void watch(ActorRef actor);

    void unwatch(ActorRef actor);

    List<ActorRef> getChildren();
}
