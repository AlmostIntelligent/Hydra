package org.gethydrated.hydra.actors.internal.actors;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.api.event.InputEvent;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StdInActor extends Actor {

	private volatile boolean running = true;
	
	private final Logger logger = getLogger(StdInActor.class);

	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof String) {
			getSystem().getEventStream().publish(new InputEvent((String) message, getSelf().toString()));
		}
	}

	@Override
	public void onStart() throws Exception {
		Runnable r = new Runnable() {

			@Override
			public void run() {
			    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				while (running) {
					String line;
                    try {
                        line = in.readLine();
                        if(line == null) {
                            running = false;
                        } else {
                            if(!line.isEmpty()) {
                                getSelf().tell(line, null);
                            }
                        }
                    } catch (IOException e) {
                        logger.warn("Error reading system.in.", e);
                        running = false;
                    }
				}
			}
			
		};
		Thread t = new Thread(r);
		t.setDaemon(true);
		t.start();
	}
	
	@Override
	public void onStop() throws Exception {
		running = false;
	}
}
