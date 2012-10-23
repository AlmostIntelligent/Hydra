package org.gethydrated.hydra.actors;

import org.gethydrated.hydra.actors.cell.StandardActorFactory;

/**
 * 
 * @author Christian Kulpa
 * 
 */
public final class System {

    boolean running;
    
    private System(String name) {
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
    
    public static System createSystem() {
        return createSystem("default");
    }
    
    public static System createSystem(String name) {
        return new System(name);
    }
    
}
