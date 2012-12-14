package org.gethydrated.hydra.actors.internal;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.mailbox.Message;
import org.gethydrated.hydra.actors.node.ActorNode;

public interface InternalRef extends ActorRef {

    void start();
    void stop();
    void restart();
    void pause();
    void resume();
    void tellSystem(Object o, ActorRef sender);

    InternalRef parent();
    ActorNode unwrap();

    boolean isTerminated();
}
