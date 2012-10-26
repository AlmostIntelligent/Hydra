package org.gethydrated.hydra.actors;

import org.gethydrated.hydra.actors.logging.LogLevel;

public class Config {
    private LogLevel loglevel = LogLevel.TRACE;
    
    public LogLevel getLogLevel() {
        return loglevel;
    }
}
