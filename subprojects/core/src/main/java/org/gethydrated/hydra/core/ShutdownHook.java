package org.gethydrated.hydra.core;

import org.gethydrated.hydra.api.Hydra;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutdownHook implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);
	
	private Thread hook = new Thread(this);
	
	private Hydra hydra;
	
	public ShutdownHook(Hydra h) {
		hydra = h;
	}
	
	@Override
	public void run() {
		logger.info("JVM Shutdown detected.");
		hydra.stop();
	}
	
	public void register() {
		logger.debug("Registering shutdown hook.");
		Runtime.getRuntime().addShutdownHook(hook);
	}
	
	public boolean unregister() {
		logger.debug("Unregistering shutdown hook.");
		try {
			return Runtime.getRuntime().removeShutdownHook(hook);
		}
		catch (IllegalStateException e) {
			return false;
		}
	}

}
