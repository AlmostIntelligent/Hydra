package org.gethydrated.hydra.api;

/**
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 */
public interface Hydra extends HydraApi {

    /**
     * Stops the Hydra.
     */
    void shutdown();

    /**
     * Awaits Hydra shutdown.
     * @throws InterruptedException thrown if interrupted while waiting.
     */
    void await() throws InterruptedException;
}
