package org.gethydrated.hydra.actors.node;

import org.gethydrated.hydra.actors.*;

import java.util.*;

/**
 *
 */
public class Children {

    private final InternalRef self;

    private final ActorSystem actorSystem;

    private final  Map<String, InternalRef> children = new HashMap<>();

    public Children(InternalRef self, ActorSystem actorSystem) {
        this.self = self;
        this.actorSystem = actorSystem;
    }

    public synchronized InternalRef getChild(String name) {
        return children.get(name);
    }

    public synchronized InternalRef addChild(String name, ActorFactory actorFactory) {
        if(children.containsKey(name)) {
            throw new RuntimeException("Actorname already in use: '" + name + "' at '" + self + "'");
        }
        ActorPath childPath = self.getPath().createChild(name);
        ActorNodeRef child = new ActorNodeRef(name, actorFactory, self, actorSystem);
        children.put(name, child);
        child.start();
        //child.tellSystem(new Create(), self);
        return child;
    }

    public synchronized void attachChild(InternalRef child) {
        if(children.containsKey(child.getName())) {
            throw new RuntimeException("Actorname already in use: '" + child.getName() + "' at '" + self + "'");
        }
        children.put(child.getName(), child);
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
    public List<ActorRef> getAllChildren() {
        LinkedList<ActorRef> res = new LinkedList<>();
        for(ActorRef r : children.values()) {
            res.add(r);
        }
        return res;
    }
}
