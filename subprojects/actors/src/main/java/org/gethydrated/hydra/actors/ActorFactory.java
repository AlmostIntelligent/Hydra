package org.gethydrated.hydra.actors;

/**
 * Factory for actor instances.
 */
public interface ActorFactory {
    /**
     * Creates a new actor instance on each call.
     * @return new actor instance.
     * @throws Exception on initialization error.
     */
    Actor create() throws Exception;
}
