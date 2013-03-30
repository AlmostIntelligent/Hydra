package org.gethydrated.hydra.actors;

import org.gethydrated.hydra.actors.actors.RootGuardian;
import org.gethydrated.hydra.actors.refs.InternalRef;

import java.util.List;

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
