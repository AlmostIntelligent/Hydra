package org.gethydrated.hydra.actors;

import java.util.List;

import org.gethydrated.hydra.actors.actors.RootGuardian;
import org.gethydrated.hydra.actors.refs.InternalRef;

/**
 *
 */
public interface ActorCreator {

    RootGuardian getRootGuardian();

    InternalRef getSysGuardian();

    InternalRef getAppGuardian();

    ActorPath createTempPath();

    void registerTempActor(InternalRef actor, ActorPath path);

    void unregisterTempActor(ActorPath path);

    ActorSystem getActorSystem();

    ActorRef getTempActor(List<String> names);
}
