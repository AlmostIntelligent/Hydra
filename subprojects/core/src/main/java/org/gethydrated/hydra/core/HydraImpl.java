package org.gethydrated.hydra.core;

import org.gethydrated.hydra.api.Hydra;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HydraImpl implements Hydra {

	private static final Logger  logger = LoggerFactory.getLogger(Hydra.class);
	
	private final ShutdownHook shutdownhook = new ShutdownHook(this);
	
	@Override
	public void start() {
		logger.info("Starting Hydra.");
		shutdownhook.register();
	}

	@Override
	public void stop() {
		logger.info("Stopping Hydra.");
		shutdownhook.unregister();
	}

}
