package org.gethydrated.hydra.actors;

/**
 * 
 * @author Christian Kulpa
 * 
 */
public abstract class Actor {

    /**
     * Event handling method.
     * @param m received object.
     * @throws Exception on exception.
     */
    public abstract void receive(Object m) throws Exception;

    /**
     * OnStart hook.
     */
    protected void onStart() {
    }

    /**
     * PreRestart hook.
     */
    protected void preRestart() {
    }

    /**
     * PostRestart hook.
     */
    protected void postRestart() {
    }

    /**
     * OnStop hook.
     */
    protected void onStop() {
    }

}
