package org.gethydrated.hydra.actors.event;

import org.gethydrated.hydra.actors.ActorRef;

public interface EventStream {

	public boolean subscribe(ActorRef subscriber, Class<?> classifier);
	
	public boolean subscribe(EventListener subscriber, Class<?> classifier);
	
	public boolean unsubscribe(ActorRef subscriber, Class<?> classifier);
	
	public boolean unsubscribe(EventListener subscriber, Class<?> classifier);
	
	public boolean unsubscribe(ActorRef subscriber);
	
	public boolean unsubscribe(EventListener subscriber);
	
	public boolean publish(Object event);
}
