package org.gethydrated.hydra.actors;

public interface ActorSource {

	ActorRef spawnActor(Class<? extends Actor> actorClass, String name);
	ActorRef spawnActor(ActorFactory actorFactory, String name);
	
	ActorRef getActor(String uri);
	ActorRef getActor(ActorURI uri);
}
