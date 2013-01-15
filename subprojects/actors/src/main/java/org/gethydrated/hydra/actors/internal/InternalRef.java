package org.gethydrated.hydra.actors.internal;

import org.gethydrated.hydra.actors.ActorRef;

public interface InternalRef extends ActorRef {

    void start();
    void stop();
    void restart();
    void pause();
    void resume();
    void watch(ActorRef target);
    void unwatch(ActorRef target);

    void tellSystem(Object o, ActorRef sender);
}
