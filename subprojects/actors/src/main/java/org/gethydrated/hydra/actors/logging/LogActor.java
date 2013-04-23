package org.gethydrated.hydra.actors.logging;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.api.event.LogEvent;
import org.slf4j.LoggerFactory;

/**
 * Log actor. 
 * 
 * Logs incoming LogEvent messages with
 * a logger of the LogEvent source class
 * name.
 *  
 * @author Christian Kulpa
 * @since 0.2.0
 */
public class LogActor extends Actor {

    @Override
    public void onReceive(final Object message) throws Exception {
        if (message instanceof LogEvent) {
            ((LogEvent) message).logInto(LoggerFactory
                    .getLogger(((LogEvent) message).getSource()));
        }
    }

    @Override
    public void onStart() {
        getLogger(LogActor.class).info("Log actor startet.");
        getSystem().getEventStream().subscribe(getSelf(), LogEvent.class);
        getSystem().getEventStream().unsubscribe(new FallbackLogger());
    }

    @Override
    public void onStop() {
        getSystem().getEventStream().subscribe(new FallbackLogger(),
                LogEvent.class);
        getSystem().getEventStream().unsubscribe(getSelf());
    }

}
