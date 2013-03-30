package org.gethydrated.hydra.actors.dispatch;

import org.gethydrated.hydra.api.configuration.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Dispatchers {

    private final Thread.UncaughtExceptionHandler handler;

    private final Configuration cfg;

    private final Map<String, Dispatcher> dispatchers = new HashMap<>();

    public Dispatchers(Configuration cfg, Thread.UncaughtExceptionHandler handler) {
        this.cfg = cfg;
        this.handler = handler;

        Dispatcher d = new SharedDispatcher(handler);
        dispatchers.put("todo", d); //TODO:Implement dispatcher naming
    }

    public Dispatcher lookupDispatcher(String name) {
        return dispatchers.get("todo");
    }

    public void join() {
        for(Dispatcher d : dispatchers.values()) {
            d.join();
        }
    }

    public void shutdown() {
        for(Dispatcher d : dispatchers.values()) {
            d.shutdown();
        }

    }
}
