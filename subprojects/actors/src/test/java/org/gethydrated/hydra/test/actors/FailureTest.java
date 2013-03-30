package org.gethydrated.hydra.test.actors;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorSystem;

/**
 *
 */
public class FailureTest {

    public static void main(String args[]) throws InterruptedException {
        ActorSystem system = ActorSystem.create();
        ActorRef ref = system.spawnActor(FailingActor.class, "test");

        Thread.sleep(2000);
        try {
            ActorRef ref2 = system.spawnActor(FailingActor.class, "test");


            Thread.sleep(2000);
        } finally {
            system.shutdown();
            system.await();
        }

    }

    public static class FailingActor extends Actor {

        @Override
        public void onReceive(Object message) throws Exception {

        }

        @Override
        public void onStart() {
            throw new RuntimeException();
        }
    }
}
