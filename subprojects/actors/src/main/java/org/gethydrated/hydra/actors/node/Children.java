package org.gethydrated.hydra.actors.node;

import org.gethydrated.hydra.actors.*;
import org.gethydrated.hydra.actors.refs.ActorNodeRef;
import org.gethydrated.hydra.actors.refs.InternalRef;

import java.util.*;

/**
 *
 */
public class Children {

    private final InternalRef self;

    private final ActorCreator actorCreator;

    private final  Map<String, InternalRef> children = new HashMap<>();

    public Children(InternalRef self, ActorCreator actorCreator) {
        this.self = self;
        this.actorCreator = actorCreator;
    }

    public synchronized InternalRef getChild(String name) {
        return children.get(name);
    }

    public synchronized InternalRef addChild(String name, ActorFactory actorFactory) {
        if(children.containsKey(name)) {
            throw new RuntimeException("Actorname already in use: '" + name + "' at '" + self + "'");
        }
        ActorNodeRef child = new ActorNodeRef(name, actorFactory, self, actorCreator);
        children.put(name, child);
        child.start();
        return child;
    }

    public synchronized boolean removeChild(ActorPath path) {
        return children.remove(path.getName()) != null;
    }

    public synchronized void stopChildren() {
        for (InternalRef ir : children.values()) {
            ir.stop();
        }
    }

    public synchronized boolean isEmpty() {
        return children.isEmpty();
    }

    /**
     * Returns a list of all names.
     * @return list of all names.
     */
    public synchronized List<ActorRef> getAllChildren() {
        LinkedList<ActorRef> res = new LinkedList<>();
        for(ActorRef r : children.values()) {
            res.add(r);
        }
        return res;
    }

    public synchronized void suspendChildren() {
        for (InternalRef i : children.values()) {
            i.suspend();
        }
    }

    public synchronized void resumeChildren(Throwable cause) {
        for (InternalRef i : children.values()) {
            i.resume(cause);
        }
    }
}
