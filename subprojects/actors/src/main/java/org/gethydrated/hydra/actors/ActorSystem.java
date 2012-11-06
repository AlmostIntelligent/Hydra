package org.gethydrated.hydra.actors;

import org.gethydrated.hydra.actors.event.EventStream;
import org.gethydrated.hydra.actors.event.SystemEventStream;
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
    
    private final ActorNode sysGuardian;
    
    private final ActorNode appGuardian;
    
    private ActorSystem() {
    	logger.info("Creating actor system.");
    	rootGuardian = new ActorNode("", new StandardActorFactory(RootGuardian.class), null, this);
    	sysGuardian = rootGuardian.getChildByName("sys");
    	appGuardian = rootGuardian.getChildByName("app");
    	eventStream.startEventHandling(1);
    }
    
    public void shutdown() {
    	eventStream.stopEventHandling();
    	rootGuardian.stop();
    	
    }

    public void await() {

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActorRef spawnActor(ActorFactory actorFactory, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActorRef getActor(String uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActorRef getActor(ActorURI uri) {
		// TODO Auto-generated method stub
		return null;
	}

}
