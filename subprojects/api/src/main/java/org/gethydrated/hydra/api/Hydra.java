package org.gethydrated.hydra.api;

/**
 * 
 * @author chris
 * @since 0.1.0
 */
public interface Hydra extends HydraApi {

    /**
     * Starts the Hydra.
     */
    void start();

    /**
     * Stops the Hydra.
     */
    void stop();
}
