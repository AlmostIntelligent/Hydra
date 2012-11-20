package org.gethydrated.hydra.actors.internal;

import java.util.Scanner;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.api.events.InputEvent;

public class StdInActor extends Actor {

	private volatile boolean running = true;
	
	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof String) {
			getSystem().getEventStream().publish(new InputEvent((String) message));
		}
	}

	@Override
	public void onStart() throws Exception {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				Scanner sc = new Scanner(System.in);
				while (running) {
					String line = sc.nextLine();
					getSelf().tell(line, null);
				}
				sc.close();
			}
			
		};
		getContext().getExecutor().execute( r );
	}
	
	@Override
	public void onStop() throws Exception {
		running = false;
	}
}
