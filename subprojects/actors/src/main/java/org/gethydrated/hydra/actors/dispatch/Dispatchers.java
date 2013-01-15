package org.gethydrated.hydra.actors.dispatch;

import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.mailbox.Mailbox;
import org.gethydrated.hydra.actors.mailbox.MailboxLookup;
import org.gethydrated.hydra.api.configuration.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Dispatchers implements MailboxLookup{

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

    @Override
    public Mailbox lookupMailbox(ActorPath path) {
        if(path.isRoot()) {
            throw new IllegalArgumentException("Cannot resolve Mailbox for root.");
        }
        for(Dispatcher d : dispatchers.values()) {
            Mailbox m = d.lookupMailbox(path);
            if(m != null) {
                return m;
            }
        }
        return null;
    }
}
