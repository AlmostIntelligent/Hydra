package org.gethydrated.hydra.actors;

import java.util.List;

/**
 * Actor source method definitions.
 */
public interface ActorSource {

    /**
     * Spawns an actor using the given class. The actor instance creation is
     * done by a DefaultActorFactory.
     * 
     * @param actorClass
     *            Actor class. Must have a public no parameter constructor.
     * @param name
     *            name of the actor.
     * @return ActorRef pointing to the new actor.
     */
    ActorRef spawnActor(Class<? extends Actor> actorClass, String name);

    /**
     * Spawns an actor using the given actor factory.
     * 
     * @param actorFactory
     *            Actor factory for new actor instances.
     * @param name
     *            name of the actor.
     * @return ActorRef pointing to the new actor.
     */
    ActorRef spawnActor(ActorFactory actorFactory, String name);

    /**
     * Retrieves an actor reference to the given actor path. Will throw an
     * ActorNotFoundException otherwise.
     * 
     * @param uri
     *            Actor path as string.
     * @return ActorRef pointing to the actor.
     */
    ActorRef getActor(String uri);

    /**
     * Retrieves an actor reference to the given actor path. Will throw an
     * ActorNotFoundException otherwise.
     * 
     * @param path
     *            Actor path.
     * @return ActorRef pointing to the actor.
     */
    ActorRef getActor(ActorPath path);

    /**
     * Retrieves an actor reference to the given actor path. Will throw an
     * ActorNotFoundException otherwise.
     * 
     * @param names
     *            Actor path as list of names.
     * @return ActorRef pointing to the actor.
     */
    ActorRef getActor(List<String> names);

    void stopActor(ActorRef ref);
}
