package org.gethydrated.hydra.actors.event;

import org.gethydrated.hydra.actors.Reference;

public class EventStream {

	public boolean subscribe(Reference subscriber, Class<?> classifier) {
		return false;
	}
	
	public boolean subscribe(EventListener subscriber, Class<?> classifier) {
		return false;
	}
	
	public boolean unsubscribe(Reference subscriber, Class<?> classifier) {
		return false;
	}
	
	public boolean unsubscribe(EventListener subscriber, Class<?> classifier) {
		return false;
	}
	
	public boolean unsubscribe(Reference subscriber) {
		return false;
	}
	
	public boolean unsubscribe(EventListener subscriber) {
		return false;
	}
	
	public boolean publish(Event event) {
		return false;
	}
	
	
}
