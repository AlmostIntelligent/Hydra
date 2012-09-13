package org.gethydrated.hydra.actors;

/**
 * 
 * @author Christian Kulpa
 * 
 */
public final class ActorSystem {

    /**
     * State.
     */
    private boolean running = true;
    
    private ActorReference rootGuard;
    private ActorReference sysGuard;
    private ActorReference appGuard;

    /**
     * Creates a new ActorSystem.
     */
    private ActorSystem() {
    }

    /**
     * Starts the shutdown sequence for the ActorSystem. After shutdown the
     * ActorSystem cannot be restartet.
     */
    public void shutdown() {
        running = false;

    }

    /**
     * Returns the state of the ActorSystem. This method will return true after
     * the shutdown is completed. There can be a delay between the shutdown call
     * and the change of this methods result.
     * 
     * If this method returns true it will never be false again for the specific
     * instance of ActorSystem.
     * 
     * @return true when the ActorSystem was completely shut down.
     */
    public boolean isTerminated() {
        return !running;
    }

    /**
     * Waits until the ActorSystem is shut down.
     */
    public void awaitTermination() {
        // TODO Auto-generated method stub
    }

    /**
     * Spawns a new top level Actor with a given name. If the given
     * name already exists the actor will be replaced by the new one.
     * @param actor new actor
     * @param name actor name
     * @return ActorReference to the new actor.
     */
    public ActorReference spawn(final Actor actor, final String name) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Creates a new ActorSystem.
     * 
     * @return new ActorSystem
     */
    public static ActorSystem createTopology() {
        return new ActorSystem();
    }
}
