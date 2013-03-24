package org.gethydrated.hydra.api.event;

public final class InputEvent implements SystemEvent {

	private final String input;

    private final String source;
	
	public InputEvent(String s, String source) {
		this.input = s;
        this.source = source;
	}

    public String getSource() {
        return source;
    }

	public String toString() {
		return input;
	}
}
