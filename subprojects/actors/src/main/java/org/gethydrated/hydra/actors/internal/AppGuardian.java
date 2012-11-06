package org.gethydrated.hydra.actors.internal;

import org.gethydrated.hydra.actors.Actor;
import org.slf4j.Logger;

public class AppGuardian extends Actor {

	private final Logger logger = getLogger(AppGuardian.class);
	
	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart() throws Exception {
		logger.info("Application guardian startet.");
	}
}
