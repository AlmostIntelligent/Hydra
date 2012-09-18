package org.gethydrated.hydra.actors.node;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorFactory;

public class StandardActorFactory implements ActorFactory {

    Class<? extends Actor> actorClass;
    
    public StandardActorFactory(Class<? extends Actor> actorClass) {
        this.actorClass = actorClass;
    }
    
    @Override
    public Actor create() {
        return null;
    }

}
