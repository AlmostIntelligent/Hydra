package org.gethydrated.hydra.api.event;

public final class InputEvent {

	private final String input;
	
	public InputEvent(String s) {
		input = s;
	}
	
	public String toString() {
		return input;
	}
}
