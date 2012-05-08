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
	public void start();

	/**
	 * Stops the Hydra.
	 */
	public void stop();
}
