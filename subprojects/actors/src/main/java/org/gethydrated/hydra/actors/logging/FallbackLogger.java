package org.gethydrated.hydra.actors.logging;

import org.gethydrated.hydra.api.event.EventListener;
import org.gethydrated.hydra.api.event.LogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fallback logger for actor system startup and shutdown.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 */
public class FallbackLogger implements EventListener {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(FallbackLogger.class);

    @Override
    public void handle(final Object event) {
        if (event instanceof LogEvent) {
            ((LogEvent) event).logInto(LOGGER);
        }
    }

    @Override
    public boolean equals(final Object o) {
        return o != null && o instanceof FallbackLogger;
    }

    @Override
    public int hashCode() {
        return FallbackLogger.class.hashCode();
    }
}
