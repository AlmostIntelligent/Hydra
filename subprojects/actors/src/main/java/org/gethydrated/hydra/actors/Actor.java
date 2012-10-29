package org.gethydrated.hydra.actors;

/**
 * Actor interface.
 * @author Christian Kulpa
 * 
 */
public abstract class Actor {

    /**
     * Message callback. Invoked on message received.
     * @param message Message object.#
     * @param ctx Actor context.
     * @throws Exception on failure.
     */
    public abstract void onReceive(Object message, Context ctx) throws Exception;
    
    /**
     * Startup callback. Gets called before message processing starts.
     * @param ctx Actor context.
     * @throws Exception on failure.
     */
    public void onStart(Context ctx) throws Exception {}
    
    /**
     * Shutdown callback. Gets called after message processing stops.
     * @param ctx Actor context.
     * @throws Exception on failure.
     */
    public void onStop(Context ctx) throws Exception {}
}
