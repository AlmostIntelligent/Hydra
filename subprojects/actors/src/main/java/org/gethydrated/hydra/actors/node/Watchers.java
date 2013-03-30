package org.gethydrated.hydra.actors.node;

import org.gethydrated.hydra.actors.SystemMessages.*;
import org.gethydrated.hydra.actors.refs.InternalRef;
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

    private volatile boolean closed = false;

    private Set<InternalRef> watched = new HashSet<>();
    private Set<InternalRef> watchers = new HashSet<>();

    public Watchers(InternalRef self, EventStream eventStream) {
        this.self = self;
        this.logger = new LoggingAdapter(Watchers.class, eventStream);
    }

    public synchronized void addWatcher(InternalRef watcher) {
        logger.debug("Actor '{}' is now watched by '{}'", self, watcher);
        if(!closed) {
            if(!watchers.contains(watcher)) {
                watchers.add(watcher);
            }
        } else {
            watcher.tellSystem(new WatcheeStopped(self), self);
        }
    }

    public synchronized void removeWatcher(InternalRef watcher) {
        if(watchers.contains(watcher)) {
            logger.debug("Actor '{}' is no longer watched by '{}'", self, watcher);
            watchers.remove(watcher);
        }
    }

    public synchronized void addWatched(InternalRef target) {
        if(!target.equals(self)) {
            logger.debug("Actor '{)' is now watching '{}'", self, target);
            if(!watched.contains(target)) {
                target.tellSystem(new Watch(self), self);
                watched.add(target);
            }
        }
    }

    public synchronized void removeWatched(InternalRef target) {
        if(watched.contains(target)) {
            logger.debug("Actor '{}' is no longer watching by '{}'", self, target);
            target.tellSystem(new UnWatch(self), self);
            watched.remove(target);
        }
    }

    public boolean isClosed() {
        return closed;
    }

    public synchronized void close() {
        closed = true;
        informWatchers();
    }

    private void informWatchers() {
        for(InternalRef r: watchers) {
            r.tellSystem(new WatcheeStopped(self), self);
        }
    }
}
