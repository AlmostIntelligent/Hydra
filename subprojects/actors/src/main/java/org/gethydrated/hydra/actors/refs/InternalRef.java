package org.gethydrated.hydra.actors.refs;

import org.gethydrated.hydra.actors.ActorCreator;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.node.ActorNode;

import java.util.List;

public interface InternalRef extends ActorRef {

    void start();
    void stop();
    void suspend();
    void restart(Throwable cause);
    void resume(Throwable cause);

    void tellSystem(Object o, ActorRef sender);

    InternalRef getChild(String name);

    InternalRef findActor(List<String> names);

    InternalRef getParent();

    ActorCreator getCreator();

    ActorNode unwrap();
}
