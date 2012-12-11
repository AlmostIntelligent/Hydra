package org.gethydrated.hydra.test.actors;


import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.actors.SystemMessages.*;
import org.junit.rules.TestWatcher;

public class WatchTest {

    public static void main(String [] args) throws InterruptedException {
        ActorSystem as = ActorSystem.create();
        ActorRef watchee = as.spawnActor(Watchee.class, "watchee");
        ActorRef watcher = as.spawnActor(Watcher.class, "watcher");
        System.out.println("Waiting 1000 Milliseconds before stopping watchee.");
        Thread.sleep(1000);
        watchee.tell("StopYourself", null);
    }

    public static class Watcher extends Actor {

        @Override
        public void onReceive(Object message) throws Exception {
            if(message instanceof Stopped) {
                System.out.println("Watchee stopped. Shutting down!");
                getSystem().shutdown();
            }
        }

        @Override
        public void onStart() {
            getContext().watch(getSystem().getActor("/app/watchee"));
        }
    }

    public static class Watchee extends Actor {

        @Override
        public void onReceive(Object message) throws Exception {
            if(message.toString().equals("StopYourself")) {
                System.out.println("Stopping watchee.");
                getContext().stop(getSelf());
            }
        }
    }
}
