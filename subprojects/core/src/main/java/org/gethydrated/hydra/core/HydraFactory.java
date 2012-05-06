package org.gethydrated.hydra.core;

import org.gethydrated.hydra.api.Hydra;

/**
 * 
 * @author chris
 * @since 0.1.0
 */
public class HydraFactory {
	
	public static Hydra getHydra() {
		return new HydraImpl();
	}
}
