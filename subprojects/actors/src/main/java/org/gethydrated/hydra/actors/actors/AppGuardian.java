package org.gethydrated.hydra.actors.actors;

import org.gethydrated.hydra.actors.Actor;
import org.slf4j.Logger;

public class AppGuardian extends Actor {

	private final Logger logger = getLogger(AppGuardian.class);
	
	@Override
	public void onReceive(Object message) throws Exception {
	}

	@Override
	public void onStart() {
		logger.info("Application guardian startet.");
	}

    @Override
    public void onStop() {
        logger.info("Application guardian stopped.");
    }
}
