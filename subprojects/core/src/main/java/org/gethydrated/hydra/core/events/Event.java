package org.gethydrated.hydra.core.events;

public class Event {
    
    private EventType type;
    
    public Event(EventType etype) {
        type = etype;
    }
    
    public EventType getType() {
        return type;
    }
}
