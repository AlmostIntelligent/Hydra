package org.gethydrated.hydra.actors.node;

import java.util.HashSet;
import java.util.Set;

import org.gethydrated.hydra.actors.SystemMessages.UnWatch;
import org.gethydrated.hydra.actors.SystemMessages.Watch;
import org.gethydrated.hydra.actors.SystemMessages.WatcheeStopped;
import org.gethydrated.hydra.actors.logging.LoggingAdapter;
import org.gethydrated.hydra.actors.refs.InternalRef;
import org.gethydrated.hydra.api.event.EventStream;
import org.slf4j.Logger;

/**
 * Actor watching.
 */
public final class Watchers {

    private final Logger logger;

    private final InternalRef self;

    private volatile boolean closed = false;

    private final Set<InternalRef> watched = new HashSet<>();
    private final Set<InternalRef> watchers = new HashSet<>();

    /**
     * Constructor.
     * @param self actor self reference.
     * @param eventStream actor system eventstream.
     */
    public Watchers(final InternalRef self, final EventStream eventStream) {
        this.self = self;
        this.logger = new LoggingAdapter(Watchers.class, eventStream);
    }

    /**
     * Adds a watching actor.
     * @param watcher watching actor.
     */
    public synchronized void addWatcher(final InternalRef watcher) {
        logger.debug("Actor '{}' is now watched by '{}'", self, watcher);
        if (!closed) {
            if (!watchers.contains(watcher)) {
                watchers.add(watcher);
            }
        } else {
            watcher.tellSystem(new WatcheeStopped(self), self);
        }
    }

    /**
     * Removes a watching actor.
     * @param watcher watching actor.
     */
    public synchronized void removeWatcher(final InternalRef watcher) {
        if (watchers.contains(watcher)) {
            logger.debug("Actor '{}' is no longer watched by '{}'", self,
                    watcher);
            watchers.remove(watcher);
        }
    }

    /**
     * Adds a watched actor.
     * @param target watched actor.
     */
    public synchronized void addWatched(final InternalRef target) {
        if (!target.equals(self)) {
            logger.debug("Actor '{)' is now watching '{}'", self, target);
            if (!watched.contains(target)) {
                target.tellSystem(new Watch(self), self);
                watched.add(target);
            }
        }
    }

    /**
     * Removes a watched actor.
     * @param target watched actor.
     */
    public synchronized void removeWatched(final InternalRef target) {
        if (watched.contains(target)) {
            logger.debug("Actor '{}' is no longer watching by '{}'", self,
                    target);
            target.tellSystem(new UnWatch(self), self);
            watched.remove(target);
        }
    }

    /**
     * Returns if the actor watching has been stopped.
     * @return true if stopped.
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * Stops actor watching. Informs all watchers.
     */
    public synchronized void close() {
        closed = true;
        informWatchers();
    }

    private void informWatchers() {
        for (final InternalRef r : watchers) {
            r.tellSystem(new WatcheeStopped(self), self);
        }
    }
}
