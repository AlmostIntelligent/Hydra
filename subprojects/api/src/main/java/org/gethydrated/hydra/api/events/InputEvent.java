package org.gethydrated.hydra.api.events;

public final class InputEvent {

	final String input;
	
	public InputEvent(String s) {
		input = s;
	}
	
	public String toString() {
		return input;
	}
}
