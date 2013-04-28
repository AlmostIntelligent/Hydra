package org.gethydrated.hydra.actors.actors;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.api.event.OutputEvent;

/**
 * Standard out actor.
 * 
 * @author Christian Kulpa
 * @author Hanno Sternberg
 * @since 0.1.0
 */
public class StdOutActor extends Actor {

    @Override
    public void onReceive(final Object message) throws Exception {
        System.out.print(message.toString());
    }

    @Override
    public void onStart() {
        getSystem().getEventStream().subscribe(getSelf(), OutputEvent.class);
    }
}
