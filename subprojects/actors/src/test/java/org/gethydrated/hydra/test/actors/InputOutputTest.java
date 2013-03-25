package org.gethydrated.hydra.test.actors;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.api.event.InputEvent;

public class InputOutputTest {

    public static void main(String [] args) throws InterruptedException {
        ActorSystem as = ActorSystem.create();
        as.spawnActor(HelloActor.class, "helloworld");
        as.spawnActor(StopActor.class, "stopper");
        as.await();
    }
    
    
    public static class HelloActor extends Actor {

        private ActorRef out;
        
        @Override
        public void onReceive(Object message) throws Exception {
            if(message instanceof InputEvent) {
                out.tell("Hello, "+message.toString(), getSelf());
            }
        }
        
        @Override
        public void onStart() {
            out = getContext().getActor("/sys/out");
            getSystem().getEventStream().subscribe(getSelf(), InputEvent.class);
        }
    }
    
    public static class StopActor extends Actor {
        
        @Override
        public void onReceive(Object message) throws Exception {
            if(message instanceof InputEvent) {
                if(message.toString().equals("Stop")) {
                    getSystem().shutdown();
                }
            }
        }
        
        @Override
        public void onStart() {
            getSystem().getEventStream().subscribe(getSelf(), InputEvent.class);
        }
    }
}
