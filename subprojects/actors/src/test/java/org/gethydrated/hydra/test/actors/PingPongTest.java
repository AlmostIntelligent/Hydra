package org.gethydrated.hydra.test.actors;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorSystem;
import org.slf4j.Logger;

public class PingPongTest {
	
	public static void main(String [] args) throws InterruptedException {
        ActorSystem as = ActorSystem.create();
        ActorRef r = as.spawnActor(PingPong.class, "pingpong");
        //Thread.sleep(1000);
        r.tell("start", null);
        as.await();
    }
	
	public static class PingPong extends Actor {

		private Logger log = getLogger(PingPong.class);
		
		ActorRef ping;
		ActorRef pong;
		
		@Override
		public void onReceive(Object message) throws Exception {
			if(message instanceof String) {
				switch ((String)message) {
				case "start":
					log.info("starting ping pong");
					ping.tell("start", getSelf());
				}
			}
		}
		
		@Override
		public void onStart() {
			log.info("creating ping pong actor");
			ping = getContext().spawnActor(Ping.class, "ping");
			pong = getContext().spawnActor(Pong.class, "pong");
		}
	}
	
	public static class Ping extends Actor {

		private Logger log = getLogger(Pong.class);
		
		@Override
		public void onReceive(Object message) throws Exception {
			if(message instanceof String) {
				switch ((String)message) {
				case "start":
					log.info("start received");
					getContext().getActor("../pong").tell("ping", getSelf());
					break;
				case "pong":
					log.info("pong received");
					getSystem().shutdown();
				}
			}
		}
		
		@Override
		public void onStart() {
			log.info("creating ping actor");
		}
	}
	
	public static class Pong extends Actor {

		private Logger log = getLogger(Ping.class);
		
		@Override
		public void onReceive(Object message) throws Exception {
			if(message instanceof String) {
				switch ((String)message) {
				case "ping":
					log.info("ping received");
					getContext().getActor("../ping").tell("pong", getSelf());
				}
			}
		}
		
		@Override
		public void onStart() {
			log.info("creating pong actor");
		}
	}
}
