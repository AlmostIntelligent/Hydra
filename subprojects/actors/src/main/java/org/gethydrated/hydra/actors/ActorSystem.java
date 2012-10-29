package org.gethydrated.hydra.actors;

import org.gethydrated.hydra.actors.cell.StandardActorFactory;
import org.gethydrated.hydra.actors.event.EventStream;
import org.gethydrated.hydra.actors.event.SystemEventStream;

/**
 * 
 * @author Christian Kulpa
 * 
 */
public final class ActorSystem {

    boolean running;
    
    SystemEventStream eventStream = new SystemEventStream();
    
    private ActorSystem(String name) {
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

    public Reference spawnActor(Class<? extends Actor> actorClass, String name) {
        ActorFactory actorFactory = new StandardActorFactory(actorClass);
        return spawnActor(actorFactory, name);
    }
    
    public Reference spawnActor(ActorFactory actorFactory, String name) {
        return null;
    }
    
	public EventStream getEventStream() {
		return eventStream;
	}    
    
    public static ActorSystem createSystem() {
        return createSystem("default");
    }
    
    public static ActorSystem createSystem(String name) {
        return new ActorSystem(name);
    }

}
