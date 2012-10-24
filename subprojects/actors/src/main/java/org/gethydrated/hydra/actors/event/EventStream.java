package org.gethydrated.hydra.actors.event;

import org.gethydrated.hydra.actors.Reference;

public interface EventStream {

	public boolean subscribe(Reference subscriber, Class<?> classifier);
	
	public boolean subscribe(EventListener subscriber, Class<?> classifier);
	
	public boolean unsubscribe(Reference subscriber, Class<?> classifier);
	
	public boolean unsubscribe(EventListener subscriber, Class<?> classifier);
	
	public boolean unsubscribe(Reference subscriber);
	
	public boolean unsubscribe(EventListener subscriber);
	
	public boolean publish(Object event);
}
