package org.gethydrated.hydra.test.actors;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorSystem;
import org.slf4j.Logger;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FutureTest {
	
	public static void main(String [] args) throws InterruptedException {
        ActorSystem as = ActorSystem.create();
        ActorRef r = as.spawnActor(FutureActor.class, "futureprocessor");
        //Thread.sleep(1000);
        Future<?> f = r.ask("start");
        try {
            Object o = f.get();
            System.out.println(o.toString());
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        as.await();
    }
	
	public static class FutureActor extends Actor {

		private Logger log = getLogger(FutureActor.class);
		
		@Override
		public void onReceive(Object message) throws Exception {
			if(message instanceof String) {
				switch ((String)message) {
				case "start":
					log.info("starting future processing");
                    Thread.sleep(1000);
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
