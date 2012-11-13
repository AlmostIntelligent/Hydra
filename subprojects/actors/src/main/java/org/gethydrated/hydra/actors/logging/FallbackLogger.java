package org.gethydrated.hydra.actors.logging;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FallbackLogger {

	private static Logger logger = LoggerFactory.getLogger(FallbackLogger.class);
	
	public static void log(List<Object> remainingEvents) {
		for(Object o : remainingEvents) {
			if(o instanceof LogEvent) {
				((LogEvent) o).logInto(logger);
			}
		}
	}
}
