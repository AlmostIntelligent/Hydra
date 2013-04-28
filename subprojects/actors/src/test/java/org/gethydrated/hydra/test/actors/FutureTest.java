package org.gethydrated.hydra.test.actors;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorSystem;
import org.slf4j.Logger;

public class FutureTest {

    public static void main(final String[] args) throws InterruptedException {
        final ActorSystem as = ActorSystem.create();
        final ActorRef r = as.spawnActor(FutureActor.class, "futureprocessor");
        final Future<?> f = r.ask("start");
        try {
            final Object o = f.get(10, TimeUnit.SECONDS);
            System.out.println(o.toString());
        } catch (final ExecutionException e) {
            e.printStackTrace();
        } catch (final TimeoutException e) {
            e.printStackTrace();
        }
        as.shutdown();
        as.await();
    }

    public static class FutureActor extends Actor {

        private final Logger log = getLogger(FutureActor.class);

        @Override
        public void onReceive(final Object message) throws Exception {
            if (message instanceof String) {
                switch ((String) message) {
                case "start":
                    log.info("Starting future processing.");
                    log.info("Got request from:" + getSender().getPath());
                    Thread.sleep(1000);
                    log.info("Sending future result.");
                    getContext().getSender().tell("future result", getSelf());
                default:
                    break;
                }
            }
        }

        @Override
        public void onStart() {
            log.info("creating future actor");
        }
    }

}
