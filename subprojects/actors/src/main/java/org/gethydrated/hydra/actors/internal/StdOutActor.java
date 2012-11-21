package org.gethydrated.hydra.actors.internal;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.api.events.OutputEvent;

public class StdOutActor extends Actor {

	@Override
	public void onReceive(Object message) throws Exception {
		System.out.println(message.toString());
	}

	@Override
	public void onStart() {
		getSystem().getEventStream().subscribe(getSelf(), OutputEvent.class);
	}
}
