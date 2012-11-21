package org.gethydrated.hydra.api.events;

public final class InputEvent {

	private final String input;
	
	public InputEvent(String s) {
		input = s;
	}
	
	public String toString() {
		return input;
	}
}
