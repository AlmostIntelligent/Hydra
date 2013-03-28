package org.gethydrated.hydra.actors;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorFactory;


/**
 * Standard ActorFactory implementation for a given class.
 * 
 * The factory will always return a new instance of the class.
 * 
 * @author Christian Kulpa
 *
 */
public class StandardActorFactory implements ActorFactory {

    /**
     * class.
     */
    final Class<? extends Actor> actorClass;
    
    /**
     * Constructor.
     * @param actorClass The actor class.
     */
    public StandardActorFactory(Class<? extends Actor> actorClass) {
        this.actorClass = actorClass;
    }
    
    @Override
    public Actor create() throws Exception{
        return actorClass.getConstructor().newInstance();
    }

}
