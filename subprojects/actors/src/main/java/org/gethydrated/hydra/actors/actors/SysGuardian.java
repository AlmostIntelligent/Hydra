package org.gethydrated.hydra.actors.actors;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.SystemMessages;
import org.gethydrated.hydra.actors.logging.LogActor;
import org.slf4j.Logger;

public class SysGuardian extends Actor {

	final Logger logger = getLogger(SysGuardian.class);
	
	@Override
	public void onReceive(Object message) throws Exception {
        if(message instanceof SystemMessages.WatcheeStopped) {
            logger.info("WatcheeStopped received.");
            getContext().getSelf();
        }
	}

	@Override
	public void onStart() throws Exception {
		logger.info("System guardian started.");
		getContext().spawnActor(LogActor.class, "log").getName();
        getContext().spawnActor(StdOutActor.class, "out");
		getContext().spawnActor(StdInActor.class, "in");
	}

    @Override
    public void onStop() {
        logger.info("System guardian stopped.");
    }
}
