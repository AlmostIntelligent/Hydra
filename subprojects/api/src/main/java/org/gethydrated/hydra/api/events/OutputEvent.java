package org.gethydrated.hydra.api.events;

public final class OutputEvent {

    private final String input;
    
    public OutputEvent(String s) {
        input = s;
    }
    
    public String toString() {
        return input;
    }
}
