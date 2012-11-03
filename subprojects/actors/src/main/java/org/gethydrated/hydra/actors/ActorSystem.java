package org.gethydrated.hydra.actors;

import org.gethydrated.hydra.actors.event.EventStream;
import org.gethydrated.hydra.actors.event.SystemEventStream;
import org.gethydrated.hydra.actors.node.StandardActorFactory;

/**
 * 
 * @author Christian Kulpa
 * 
 */
public final class ActorSystem {

    boolean running;
    
    SystemEventStream eventStream = new SystemEventStream();
    
    private ActorSystem() {
        running = true;

    }
    
    public void shutdown() {
    	//create EventStream
    	//create Logger
    	//start RootGuardian
    	//start	 SysGuardian
    	//start		Logger
    	//start	 AppGuardian
    	
    	//start eventhandling
    	
    	//on failure
    	// dump eventstream to logger
        running = false;
    }

    public void await() {

    }

    public boolean isTerminated() {
        return !running;
    }

    public ActorRef spawnActor(Class<? extends Actor> actorClass, String name) {
        ActorFactory actorFactory = new StandardActorFactory(actorClass);
        return spawnActor(actorFactory, name);
    }
    
    public ActorRef spawnActor(ActorFactory actorFactory, String name) {
        return null;
    }
    
	public EventStream getEventStream() {
		return eventStream;
	}

    public static ActorSystem create() {
        return new ActorSystem();
    }

}
