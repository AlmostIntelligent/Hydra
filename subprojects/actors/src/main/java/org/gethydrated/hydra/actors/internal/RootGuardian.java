package org.gethydrated.hydra.actors.internal;

import org.gethydrated.hydra.actors.Actor;
import org.slf4j.Logger;

public class RootGuardian extends Actor {

	private final Logger logger = getLogger(RootGuardian.class);
	
	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart() {
		logger.info("Root guardian started.");
		getContext().spawnActor(SysGuardian.class, "sys");
		getContext().spawnActor(AppGuardian.class, "app");
	}
}
