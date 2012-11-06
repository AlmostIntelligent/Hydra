package org.gethydrated.hydra.actors.internal;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.logging.LogActor;
import org.slf4j.Logger;

public class SysGuardian extends Actor {

	Logger logger = getLogger(SysGuardian.class);
	
	@Override
	public void onReceive(Object message) throws Exception {
		System.out.println(message);
	}

	@Override
	public void onStart() throws Exception {
		logger.info("System guardian started.");
		getContext().spawnActor(LogActor.class, "log");
	}
}
