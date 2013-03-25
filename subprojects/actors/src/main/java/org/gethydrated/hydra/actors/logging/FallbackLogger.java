package org.gethydrated.hydra.actors.logging;

import org.gethydrated.hydra.api.event.EventListener;
import org.gethydrated.hydra.api.event.LogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FallbackLogger implements EventListener {

	private static final Logger logger = LoggerFactory.getLogger(FallbackLogger.class);

    @Override
    public void handle(Object event) {
        if(event instanceof LogEvent) {
            ((LogEvent) event).logInto(logger);
        }
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof FallbackLogger;
    }

    @Override
    public int hashCode() {
        return FallbackLogger.class.hashCode();
    }
}
