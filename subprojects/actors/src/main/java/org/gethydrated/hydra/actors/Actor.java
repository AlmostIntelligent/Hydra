package org.gethydrated.hydra.actors;

/**
 * Actor interface.
 * @author Christian Kulpa
 * 
 */
public interface Actor {

    /**
     * Message callback. Invoked on message received.
     * @param message Message object.#
     * @param ctx Actor context.
     * @throws Exception on failure.
     */
    void receive(Object message, Context ctx) throws Exception;
    
    /**
     * Startup callback. Gets called before message processing starts.
     * @param ctx Actor context.
     * @throws Exception on failure.
     */
    void onStart(Context ctx) throws Exception;
    
    /**
     * Shutdown callback. Gets called after message processing stops.
     * @param ctx Actor context.
     * @throws Exception on failure.
     */
    void onStop(Context ctx) throws Exception;
}
