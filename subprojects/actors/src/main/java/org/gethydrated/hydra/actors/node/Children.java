package org.gethydrated.hydra.actors.node;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.gethydrated.hydra.actors.ActorCreator;
import org.gethydrated.hydra.actors.ActorFactory;
import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.refs.ActorNodeRef;
import org.gethydrated.hydra.actors.refs.InternalRef;

/**
 *
 */
public final class Children {

    private final InternalRef self;

    private final ActorCreator actorCreator;

    private final Map<String, InternalRef> children = new HashMap<>();

    public Children(final InternalRef self, final ActorCreator actorCreator) {
        this.self = self;
        this.actorCreator = actorCreator;
    }

    public synchronized InternalRef getChild(final String name) {
        return children.get(name);
    }

    public synchronized InternalRef addChild(final String name,
            final ActorFactory actorFactory) {
        if (children.containsKey(name)) {
            throw new RuntimeException("Actorname already in use: '" + name
                    + "' at '" + self + "'");
        }
        final ActorNodeRef child = new ActorNodeRef(name, actorFactory, self,
                actorCreator);
        children.put(name, child);
        child.start();
        return child;
    }

    public synchronized boolean removeChild(final ActorPath path) {
        return children.remove(path.getName()) != null;
    }

    public synchronized void stopChildren() {
        for (final InternalRef ir : children.values()) {
            ir.stop();
        }
    }

    public synchronized boolean isEmpty() {
        return children.isEmpty();
    }

    /**
     * Returns a list of all names.
     * 
     * @return list of all names.
     */
    public synchronized List<ActorRef> getAllChildren() {
        final LinkedList<ActorRef> res = new LinkedList<>();
        for (final ActorRef r : children.values()) {
            res.add(r);
        }
        return res;
    }

    public synchronized void suspendChildren() {
        for (final InternalRef i : children.values()) {
            i.suspend();
        }
    }

    public synchronized void resumeChildren(final Throwable cause) {
        for (final InternalRef i : children.values()) {
            i.resume(cause);
        }
    }
}
