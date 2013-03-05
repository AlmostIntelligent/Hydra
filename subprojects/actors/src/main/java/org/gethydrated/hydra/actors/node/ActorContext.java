package org.gethydrated.hydra.actors.node;

import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorSource;

import java.util.List;

public interface ActorContext extends ActorSource {

	String getName();

    ActorRef getSender();

    void stop(ActorRef target);

    void watch(ActorRef target);

    void unwatch(ActorRef target);

    ActorRef getActor(ActorPath path);

    List<String> getChildren();
}
