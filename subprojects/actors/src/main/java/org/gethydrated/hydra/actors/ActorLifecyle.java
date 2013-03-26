package org.gethydrated.hydra.actors;

/**
 * Actor lifecycle states.
 */
public enum ActorLifecyle {
    /**
     * Actor was created but not yet startet.
     */
    CREATED,
    /**
     * Actor is starting.
     */
    STARTING,
    /**
     * Actor started successfully.
     */
    RUNNING,
    /**
     * Actor is suspended and will not process any user messages.
     */
    SUSPENDED,
    /**
     * Actor restarts.
     */
    RESTARTING,
    /**
     * Actor stops.
     */
    STOPPING,
    /**
     * Actor is stopped. If an actor is in the stopped state,
     * there cannot be any more state changes.
     */
    STOPPED,
    /**
     * An error occurred while processing a message.
     */
    ERROR
}
