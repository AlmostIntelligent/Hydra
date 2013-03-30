package org.gethydrated.hydra.test.actors;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorSystem;
import org.slf4j.Logger;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureTest {
	
	public static void main(String [] args) throws InterruptedException {
        ActorSystem as = ActorSystem.create();
        ActorRef r = as.spawnActor(FutureActor.class, "futureprocessor");
        Future<?> f = r.ask("start");
        try {
            Object o = f.get(10, TimeUnit.SECONDS);
            System.out.println(o.toString());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        as.shutdown();
        as.await();
    }

	public static class FutureActor extends Actor {

		private final Logger log = getLogger(FutureActor.class);
		
		@Override
		public void onReceive(Object message) throws Exception {
			if(message instanceof String) {
				switch ((String)message) {
				case "start":
					log.info("Starting future processing.");
                    log.info("Got request from:" + getSender().getPath());
                    Thread.sleep(1000);
                    log.info("Sending future result.");
					getContext().getSender().tell("future result", getSelf());
				}
			}
		}
		
		@Override
		public void onStart() {
			log.info("creating future actor");
		}
	}

}
