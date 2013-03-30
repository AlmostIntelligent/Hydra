package org.gethydrated.hydra.actors.logging;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.api.event.LogEvent;
import org.slf4j.LoggerFactory;

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
        getSystem().getEventStream().unsubscribe(new FallbackLogger());
	}
	
	public void onStop() {
	    getSystem().getEventStream().subscribe(new FallbackLogger(), LogEvent.class);
        getSystem().getEventStream().unsubscribe(getSelf());
	}
	
}
