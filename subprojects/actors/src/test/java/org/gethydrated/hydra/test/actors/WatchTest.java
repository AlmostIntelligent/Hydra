package org.gethydrated.hydra.test.actors;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorSystem;

public class WatchTest {

    public static void main(final String[] args) throws InterruptedException {
        final ActorSystem as = ActorSystem.create();
        final ActorRef watchee = as.spawnActor(Watchee.class, "watchee");
        as.spawnActor(Watcher.class, "watcher");
        System.out
                .println("Waiting 1000 Milliseconds before stopping watchee.");
        Thread.sleep(1000);
        watchee.tell("StopYourself", null);
        as.await();
    }

    public static class Watcher extends Actor {

        @Override
        public void onReceive(final Object message) throws Exception {
        }

        @Override
        public void onStart() {
            getContext().watch(getSystem().getActor("/app/watchee"));
        }

        @Override
        public void onStop() {
            System.out.println("Watchee stopped. Shutting down!");
            getSystem().shutdown();
        }
    }

    public static class Watchee extends Actor {

        @Override
        public void onReceive(final Object message) throws Exception {
            if (message.toString().equals("StopYourself")) {
                System.out.println("Stopping watchee.");
                getContext().stopActor(getSelf());
            }
        }
    }
}
