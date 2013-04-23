package org.gethydrated.hydra.actors.dispatch;

import java.util.HashMap;
import java.util.Map;

import org.gethydrated.hydra.api.configuration.Configuration;

/**
 *
 */
public class Dispatchers {

    //private final Thread.UncaughtExceptionHandler handler;

    //private final Configuration cfg;

    private final Map<String, Dispatcher> dispatchers = new HashMap<>();

    /**
     * Constructor.
     * @param cfg configuration.
     * @param handler exception handler.
     */
    public Dispatchers(final Configuration cfg,
            final Thread.UncaughtExceptionHandler handler) {
        //this.cfg = cfg;
        //this.handler = handler;

        final Dispatcher d = new SharedDispatcher(handler);
        dispatchers.put("todo", d);
    }

    /**
     * Looks up a specific dispatcher by name.
     * @param name dispatcher name.
     * @return dispatcher.
     */
    public Dispatcher lookupDispatcher(final String name) {
        return dispatchers.get("todo");
    }

    /**
     * Joins all dispatcher threads.
     */
    public void join() {
        for (final Dispatcher d : dispatchers.values()) {
            d.join();
        }
    }

    /**
     * Shuts all dispatchers down.
     */
    public void shutdown() {
        for (final Dispatcher d : dispatchers.values()) {
            d.shutdown();
        }

    }
}
