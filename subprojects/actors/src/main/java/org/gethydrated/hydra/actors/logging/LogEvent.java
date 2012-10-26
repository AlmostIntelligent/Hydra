package org.gethydrated.hydra.actors.logging;

import org.slf4j.Marker;

public abstract class LogEvent {

    String threadname = Thread.currentThread().getName();
    
    Marker marker;
    
    public static class LogError extends LogEvent {
        
    }
    
	public static class LogDebug extends LogEvent {
		
	}
	
	public static class LogTrace extends LogEvent {
	    
	}
	
}
