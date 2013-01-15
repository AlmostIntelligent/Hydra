package org.gethydrated.hydra.actors.node;

import org.gethydrated.hydra.actors.SystemMessages.WatcheeStopped;
import org.gethydrated.hydra.actors.internal.InternalRef;
import org.gethydrated.hydra.actors.logging.LoggingAdapter;
import org.gethydrated.hydra.api.event.EventStream;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class Watchers {

    private final Logger logger;

    private final InternalRef self;

    private volatile boolean isClosed = false;

    private Set<InternalRef> watched = new HashSet<>();
    private Set<InternalRef> watchers = new HashSet<>();

    public Watchers(InternalRef self, EventStream eventStream) {
        this.self = self;
        this.logger = new LoggingAdapter(Watchers.class, eventStream);
    }

    public synchronized void addWatcher(InternalRef watcher) {
        logger.debug("Actor '{}' is now watched by '{}'", self, watcher);
    }

    public synchronized void removeWatcher(InternalRef watcher) {
        logger.debug("Actor '{}' is no longer watched by '{}'", self, watcher);
    }

    public synchronized void addWatched(InternalRef target) {
        logger.debug("Actor '{)' is now watching '{}'", self, target);
    }

    public synchronized void removeWatched(InternalRef target) {
        logger.debug("Actor '{}' is no longer watching by '{}'", self, target);
    }

    public boolean isClosed() {
        return isClosed;
    }

    public synchronized void close() {
        isClosed = true;
        informWatchers();
    }

    private void informWatchers() {
        for(InternalRef r: watchers) {
            r.tellSystem(new WatcheeStopped(self), self);
        }
    }
}
