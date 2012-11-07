package org.gethydrated.hydra.actors.logging;

import org.gethydrated.hydra.actors.Actor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class LogActor extends Actor {

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof LogEvent) {
			((LogEvent) message).logInto(LoggerFactory.getLogger(((LogEvent) message).getSource()));
		} 
	}

	@Override
	public void onStart() {
		getLogger(LogActor.class).info("Log actor startet.");
		getSystem().getEventStream().subscribe(getSelf(), LogEvent.class);
	}
	
}
