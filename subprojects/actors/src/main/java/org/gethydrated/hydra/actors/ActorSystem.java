package org.gethydrated.hydra.actors;

import org.gethydrated.hydra.actors.event.EventStream;
import org.gethydrated.hydra.actors.event.SystemEventStream;
import org.gethydrated.hydra.actors.logging.FallbackLogger;
import org.gethydrated.hydra.actors.logging.LoggingAdapter;
import org.gethydrated.hydra.actors.node.*;
import org.gethydrated.hydra.actors.internal.*;
import org.slf4j.Logger;

/**
 * 
 * @author Christian Kulpa
 * 
 */
public final class ActorSystem implements ActorSource{
    
    private final SystemEventStream eventStream = new SystemEventStream();
    
    private final Logger logger =new LoggingAdapter(ActorSystem.class,this);; 
    
    private final ActorNode rootGuardian;
    
    private final ActorNode appGuardian;
    
    private final Object awaitLock;
    
    private ActorSystem() {
    	logger.info("Creating actor system.");
    	awaitLock = new Object();
    	rootGuardian = new ActorNode("", new StandardActorFactory(RootGuardian.class), null, this);
    	appGuardian = rootGuardian.getChildByName("app");
    	eventStream.startEventHandling(1);
    }
    
    public void shutdown() {
    	eventStream.stopEventHandling();
    	rootGuardian.stop();
    	logger.info("Actor system stopped.");
    	if(eventStream.hasRemainingEvents()) {
    		FallbackLogger.log(eventStream.getRemainingEvents());
    	}
    	synchronized(awaitLock) {
    	    awaitLock.notifyAll();
    	}
    }

    public void await() throws InterruptedException {
        synchronized(awaitLock) {
            if(!isTerminated()) {
                awaitLock.wait();
            }
        }
    }

    public boolean isTerminated() {
        return rootGuardian.isTerminated();
    }
    
	public EventStream getEventStream() {
		return eventStream;
	}

    public static ActorSystem create() {
        return new ActorSystem();
    }

	@Override
	public ActorRef spawnActor(Class<? extends Actor> actorClass, String name) {
		return appGuardian.spawnActor(actorClass, name);
	}

	@Override
	public ActorRef spawnActor(ActorFactory actorFactory, String name) {
		return appGuardian.spawnActor(actorFactory, name);
	}

	@Override
	public ActorRef getActor(String uri) {
		if(uri.startsWith("/")) {
			return rootGuardian.getActor(uri.substring(1));
		} else if(uri.startsWith("..")) {
			throw new RuntimeException("Actor not found.");
		} else {
			return rootGuardian.getActor(uri);
		}
	}

	@Override
	public ActorRef getActor(ActorURI uri) {
		return getActor(uri.toString());
	}

}
