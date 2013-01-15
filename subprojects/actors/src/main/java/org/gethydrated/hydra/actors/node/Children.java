package org.gethydrated.hydra.actors.node;

import org.gethydrated.hydra.actors.*;
import org.gethydrated.hydra.actors.dispatch.Dispatchers;
import org.gethydrated.hydra.actors.internal.InternalRef;
import org.gethydrated.hydra.actors.internal.LazyActorRef;
import org.gethydrated.hydra.actors.internal.NodeRef;
import org.gethydrated.hydra.actors.internal.NodeRefImpl;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Children {

    private final InternalRef self;

    private final ActorSystem actorSystem;

    private final  Map<String, NodeRef> children = new HashMap<>();

    private final Dispatchers dispatchers;

    public Children(InternalRef self, ActorSystem actorSystem, Dispatchers dispatchers) {
        this.self = self;
        this.actorSystem = actorSystem;
        this.dispatchers = dispatchers;
    }

    public synchronized InternalRef getChild(String name) {
        return null;
    }

    public synchronized ActorRef addChild(String name, ActorFactory actorFactory) {
        if(children.containsKey(name)) {
            throw new RuntimeException("Actorname already in use: '" + name + "' at '" + self + "'");
        }
        ActorPath childPath = self.getPath().createChild(name);
        NodeRef child = new NodeRefImpl(childPath, actorFactory, self, actorSystem, dispatchers);
        children.put(name, child);
        child.start();
        return new LazyActorRef(childPath, dispatchers);
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
}
