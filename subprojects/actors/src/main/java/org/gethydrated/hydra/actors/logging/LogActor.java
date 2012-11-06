package org.gethydrated.hydra.actors.logging;

import org.gethydrated.hydra.actors.Actor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogActor extends Actor {

	private final Logger logger = LoggerFactory.getLogger(LogActor.class);

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof LogEvent) {
			((LogEvent) message).logInto(logger);
		} 
	}

	@Override
	public void onStart() {
		getLogger(LogActor.class).info("Log actor startet.");
		getSystem().getEventStream().subscribe(getSelf(), LogEvent.class);
	}
	
}
