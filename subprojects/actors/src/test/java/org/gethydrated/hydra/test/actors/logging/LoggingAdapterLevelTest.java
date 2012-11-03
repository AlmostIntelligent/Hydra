package org.gethydrated.hydra.test.actors.logging;

import static org.junit.Assert.assertTrue;

import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.actors.logging.LoggingAdapter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

public class LoggingAdapterLevelTest {
    
    private ActorSystem testSystem;
    private ch.qos.logback.classic.Logger baseLogger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger("test");
    
    @Before
    public void setup() {
    	testSystem = ActorSystem.create();
    }
    
    @After
    public void teardown() {
    	testSystem.shutdown();
    }
    
    @Test
    public void testLoggingLevelTrace() {
    	baseLogger.setLevel(Level.TRACE);
        Logger log = new LoggingAdapter("test", testSystem);
        checkLogger(baseLogger, log);
    }
    
    @Test
    public void testLoggingLevelDebug() {
    	baseLogger.setLevel(Level.DEBUG);
        Logger log = new LoggingAdapter("test", testSystem);
        checkLogger(baseLogger, log);
    }
    
    @Test
    public void testLoggingLevelInfo() {
    	baseLogger.setLevel(Level.INFO);
        Logger log = new LoggingAdapter("test", testSystem);
        checkLogger(baseLogger, log);
    }
    
    @Test
    public void testLoggingLevelWarn() {
    	baseLogger.setLevel(Level.WARN);
        Logger log = new LoggingAdapter("test", testSystem);
        checkLogger(baseLogger, log);
    }
    
    @Test
    public void testLoggingLevelError() {
    	baseLogger.setLevel(Level.ERROR);
        Logger log = new LoggingAdapter("test", testSystem);
        checkLogger(baseLogger, log);
    }
    
    @Test
    public void testLoggingLevelChange() {
    	baseLogger.setLevel(Level.DEBUG);
    	Logger log = new LoggingAdapter("test", testSystem);
    	checkLogger(baseLogger, log);
    	baseLogger.setLevel(Level.ERROR);
    	checkLogger(baseLogger, log);
    }
    
    public void checkLogger(Logger base, Logger test) {
    	assertTrue(test.isTraceEnabled() == base.isTraceEnabled());
        assertTrue(test.isDebugEnabled() == base.isDebugEnabled());
        assertTrue(test.isInfoEnabled() == base.isInfoEnabled());
        assertTrue(test.isWarnEnabled() == base.isWarnEnabled());
        assertTrue(test.isErrorEnabled()== base.isErrorEnabled());
    }
}
