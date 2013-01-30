package org.gethydrated.hydra.actors.internal.actors;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.api.event.OutputEvent;

public class StdOutActor extends Actor {

	@Override
	public void onReceive(Object message) throws Exception {
		System.out.print(message.toString());
	}

	@Override
	public void onStart() {
		getSystem().getEventStream().subscribe(getSelf(), OutputEvent.class);
	}
}
