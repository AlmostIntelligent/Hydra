package org.gethydrated.hydra.test.actors.logging;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.gethydrated.hydra.actors.event.EventStream;
import org.gethydrated.hydra.actors.event.SystemEventStream;
import org.gethydrated.hydra.actors.logging.LogLevel;
import org.gethydrated.hydra.actors.logging.LoggingAdapter;
import org.junit.Test;
import org.slf4j.Logger;

public class LoggingAdapterLevelTest {
    
    EventStream testStream = new SystemEventStream();
    
    @Test
    public void testLoggingAdapterTraceLevel() {
        Logger log = new LoggingAdapter("test", LogLevel.TRACE, testStream);
        assertTrue(log.isTraceEnabled());
        assertTrue(log.isDebugEnabled());
        assertTrue(log.isInfoEnabled());
        assertTrue(log.isWarnEnabled());
        assertTrue(log.isErrorEnabled());
    }
    @Test
    public void testLoggingAdapterDebugLevel() {
        Logger log = new LoggingAdapter("test", LogLevel.DEBUG, testStream);
        assertFalse(log.isTraceEnabled());
        assertTrue(log.isDebugEnabled());
        assertTrue(log.isInfoEnabled());
        assertTrue(log.isWarnEnabled());
        assertTrue(log.isErrorEnabled());
    }
    @Test
    public void testLoggingAdapterInfoLevel() {
        Logger log = new LoggingAdapter("test", LogLevel.INFO, testStream);
        assertFalse(log.isTraceEnabled());
        assertFalse(log.isDebugEnabled());
        assertTrue(log.isInfoEnabled());
        assertTrue(log.isWarnEnabled());
        assertTrue(log.isErrorEnabled());
    }
    @Test
    public void testLoggingAdapterWarnLevel() {
        Logger log = new LoggingAdapter("test", LogLevel.WARN, testStream);
        assertFalse(log.isTraceEnabled());
        assertFalse(log.isDebugEnabled());
        assertFalse(log.isInfoEnabled());
        assertTrue(log.isWarnEnabled());
        assertTrue(log.isErrorEnabled());
    }
    @Test
    public void testLoggingAdapterErrorLevel() {
        Logger log = new LoggingAdapter("test", LogLevel.ERROR, testStream);
        assertFalse(log.isTraceEnabled());
        assertFalse(log.isDebugEnabled());
        assertFalse(log.isInfoEnabled());
        assertFalse(log.isWarnEnabled());
        assertTrue(log.isErrorEnabled());
    }
}
