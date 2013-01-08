package org.gethydrated.hydra.api.event;

public final class OutputEvent {

    private final String input;
    
    public OutputEvent(String s) {
        input = s;
    }
    
    public String toString() {
        return input;
    }
}
