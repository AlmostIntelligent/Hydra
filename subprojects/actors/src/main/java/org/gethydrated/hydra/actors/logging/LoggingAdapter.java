package org.gethydrated.hydra.actors.logging;

import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.api.event.EventStream;
import org.gethydrated.hydra.api.event.LogEvent;
import org.gethydrated.hydra.api.event.LogEvent.LogDebug;
import org.gethydrated.hydra.api.event.LogEvent.LogError;
import org.gethydrated.hydra.api.event.LogEvent.LogInfo;
import org.gethydrated.hydra.api.event.LogEvent.LogTrace;
import org.gethydrated.hydra.api.event.LogEvent.LogWarn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

/**
 * Actor system based log adapter. Creates additional
 * informations on logging.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 */
public class LoggingAdapter implements Logger {

    private final String name;

    private final EventStream eventStream;

    private final Logger logger;

    /**
     * Constructor.
     * @param clazz class name.
     * @param system parent actor system.
     */
    public LoggingAdapter(final Class<?> clazz, final ActorSystem system) {
        this(clazz.getName(), system.getEventStream());
    }

    /**
     * Constructor.
     * @param name logger name.
     * @param system parent actor system.
     */
    public LoggingAdapter(final String name, final ActorSystem system) {
        this(name, system.getEventStream());
    }

    /**
     * Constructor.
     * @param clazz class name.
     * @param eventStream Eventstream.
     */
    public LoggingAdapter(final Class<?> clazz, final EventStream eventStream) {
        this(clazz.getName(), eventStream);
    }

    /**
     * Constructor.
     * @param name logger name.
     * @param eventStream Eventstream.
     */
    public LoggingAdapter(final String name, final EventStream eventStream) {
        this.name = name;
        logger = LoggerFactory.getLogger(name);
        this.eventStream = eventStream;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(final String msg) {
        filterAndTrace(msg, null, null, null, null);
    }

    @Override
    public void trace(final String format, final Object arg) {
        filterAndTrace(format, arg, null, null, null);
    }

    @Override
    public void trace(final String format, final Object arg1, final Object arg2) {
        filterAndTrace(format, arg1, arg2, null, null);
    }

    @Override
    public void trace(final String format, final Object[] argArray) {
        filterAndTrace(format, null, null, argArray, null);
    }

    @Override
    public void trace(final String msg, final Throwable t) {
        filterAndTrace(msg, null, null, null, t);
    }

    @Override
    public boolean isTraceEnabled(final Marker marker) {
        return logger.isTraceEnabled(marker);
    }

    @Override
    public void trace(final Marker marker, final String msg) {
        filterAndTraceM(marker, msg, null, null, null, null);
    }

    @Override
    public void trace(final Marker marker, final String format, final Object arg) {
        filterAndTraceM(marker, format, arg, null, null, null);

    }

    @Override
    public void trace(final Marker marker, final String format,
            final Object arg1, final Object arg2) {
        filterAndTraceM(marker, format, arg1, arg2, null, null);

    }

    @Override
    public void trace(final Marker marker, final String format,
            final Object[] argArray) {
        filterAndTraceM(marker, format, null, null, argArray, null);

    }

    @Override
    public void trace(final Marker marker, final String msg, final Throwable t) {
        filterAndTraceM(marker, msg, null, null, null, t);

    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(final String msg) {
        filterAndDebug(msg, null, null, null, null);
    }

    @Override
    public void debug(final String format, final Object arg) {
        filterAndDebug(format, arg, null, null, null);

    }

    @Override
    public void debug(final String format, final Object arg1, final Object arg2) {
        filterAndDebug(format, arg1, arg2, null, null);

    }

    @Override
    public void debug(final String format, final Object[] argArray) {
        filterAndDebug(format, null, null, argArray, null);

    }

    @Override
    public void debug(final String msg, final Throwable t) {
        filterAndDebug(msg, null, null, null, t);
    }

    @Override
    public boolean isDebugEnabled(final Marker marker) {
        return logger.isDebugEnabled(marker);
    }

    @Override
    public void debug(final Marker marker, final String msg) {
        filterAndDebugM(marker, msg, null, null, null, null);

    }

    @Override
    public void debug(final Marker marker, final String format, final Object arg) {
        filterAndDebugM(marker, format, arg, null, null, null);
    }

    @Override
    public void debug(final Marker marker, final String format,
            final Object arg1, final Object arg2) {
        filterAndDebugM(marker, format, arg1, arg2, null, null);
    }

    @Override
    public void debug(final Marker marker, final String format,
            final Object[] argArray) {
        filterAndDebugM(marker, format, null, null, argArray, null);
    }

    @Override
    public void debug(final Marker marker, final String msg, final Throwable t) {
        filterAndDebugM(marker, msg, null, null, null, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(final String msg) {
        filterAndInfo(msg, null, null, null, null);
    }

    @Override
    public void info(final String format, final Object arg) {
        filterAndInfo(format, arg, null, null, null);
    }

    @Override
    public void info(final String format, final Object arg1, final Object arg2) {
        filterAndInfo(format, arg1, arg2, null, null);
    }

    @Override
    public void info(final String format, final Object[] argArray) {
        filterAndInfo(format, null, null, argArray, null);
    }

    @Override
    public void info(final String msg, final Throwable t) {
        filterAndInfo(msg, null, null, null, t);
    }

    @Override
    public boolean isInfoEnabled(final Marker marker) {
        return logger.isInfoEnabled(marker);
    }

    @Override
    public void info(final Marker marker, final String msg) {
        filterAndInfoM(marker, msg, null, null, null, null);
    }

    @Override
    public void info(final Marker marker, final String format, final Object arg) {
        filterAndInfoM(marker, format, arg, null, null, null);
    }

    @Override
    public void info(final Marker marker, final String format,
            final Object arg1, final Object arg2) {
        filterAndInfoM(marker, format, arg1, arg2, null, null);
    }

    @Override
    public void info(final Marker marker, final String format,
            final Object[] argArray) {
        filterAndInfoM(marker, format, null, null, argArray, null);
    }

    @Override
    public void info(final Marker marker, final String msg, final Throwable t) {
        filterAndInfoM(marker, msg, null, null, null, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(final String msg) {
        filterAndWarn(msg, null, null, null, null);
    }

    @Override
    public void warn(final String format, final Object arg) {
        filterAndWarn(format, arg, null, null, null);
    }

    @Override
    public void warn(final String format, final Object[] argArray) {
        filterAndWarn(format, null, null, argArray, null);
    }

    @Override
    public void warn(final String format, final Object arg1, final Object arg2) {
        filterAndWarn(format, arg1, arg2, null, null);
    }

    @Override
    public void warn(final String msg, final Throwable t) {
        filterAndWarn(msg, null, null, null, t);
    }

    @Override
    public boolean isWarnEnabled(final Marker marker) {
        return logger.isWarnEnabled(marker);
    }

    @Override
    public void warn(final Marker marker, final String msg) {
        filterAndWarnM(marker, msg, null, null, null, null);
    }

    @Override
    public void warn(final Marker marker, final String format, final Object arg) {
        filterAndWarnM(marker, format, arg, null, null, null);
    }

    @Override
    public void warn(final Marker marker, final String format,
            final Object arg1, final Object arg2) {
        filterAndWarnM(marker, format, arg1, arg2, null, null);
    }

    @Override
    public void warn(final Marker marker, final String format,
            final Object[] argArray) {
        filterAndWarnM(marker, format, null, null, argArray, null);
    }

    @Override
    public void warn(final Marker marker, final String msg, final Throwable t) {
        filterAndWarnM(marker, msg, null, null, null, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(final String msg) {
        filterAndError(msg, null, null, null, null);
    }

    @Override
    public void error(final String format, final Object arg) {
        filterAndError(format, arg, null, null, null);
    }

    @Override
    public void error(final String format, final Object arg1, final Object arg2) {
        filterAndError(format, arg1, arg2, null, null);
    }

    @Override
    public void error(final String format, final Object[] argArray) {
        filterAndError(format, null, null, argArray, null);
    }

    @Override
    public void error(final String msg, final Throwable t) {
        filterAndError(msg, null, null, null, t);
    }

    @Override
    public boolean isErrorEnabled(final Marker marker) {
        return logger.isErrorEnabled(marker);
    }

    @Override
    public void error(final Marker marker, final String msg) {
        filterAndErrorM(marker, msg, null, null, null, null);
    }

    @Override
    public void error(final Marker marker, final String format, final Object arg) {
        filterAndErrorM(marker, format, arg, null, null, null);
    }

    @Override
    public void error(final Marker marker, final String format,
            final Object arg1, final Object arg2) {
        filterAndErrorM(marker, format, arg1, arg2, null, null);
    }

    @Override
    public void error(final Marker marker, final String format,
            final Object[] argArray) {
        filterAndErrorM(marker, format, null, null, argArray, null);
    }

    @Override
    public void error(final Marker marker, final String msg, final Throwable t) {
        filterAndErrorM(marker, msg, null, null, null, t);
    }

    private void filterAndTrace(final String msg, final Object arg1,
            final Object arg2, final Object[] argArray, final Throwable t) {
        if (isTraceEnabled()) {
            final LogTrace l = new LogTrace(name, msg, null, arg1, arg2,
                    argArray, t);
            eventStream.publish(l);
        }
    }

    private void filterAndTraceM(final Marker m, final String msg,
            final Object arg1, final Object arg2, final Object[] argArray,
            final Throwable t) {
        if (isTraceEnabled(m)) {
            final LogEvent l = new LogTrace(name, msg, m, arg1, arg2, argArray,
                    t);
            eventStream.publish(l);
        }
    }

    private void filterAndDebug(final String msg, final Object arg1,
            final Object arg2, final Object[] argArray, final Throwable t) {
        if (isDebugEnabled()) {
            final LogEvent l = new LogDebug(name, msg, null, arg1, arg2,
                    argArray, t);
            eventStream.publish(l);
        }
    }

    private void filterAndDebugM(final Marker m, final String msg,
            final Object arg1, final Object arg2, final Object[] argArray,
            final Throwable t) {
        if (isDebugEnabled(m)) {
            final LogEvent l = new LogDebug(name, msg, m, arg1, arg2, argArray,
                    t);
            eventStream.publish(l);
        }
    }

    private void filterAndInfo(final String msg, final Object arg1,
            final Object arg2, final Object[] argArray, final Throwable t) {
        if (isInfoEnabled()) {
            final LogEvent l = new LogInfo(name, msg, null, arg1, arg2,
                    argArray, t);
            eventStream.publish(l);
        }
    }

    private void filterAndInfoM(final Marker m, final String msg,
            final Object arg1, final Object arg2, final Object[] argArray,
            final Throwable t) {
        if (isInfoEnabled(m)) {
            final LogEvent l = new LogInfo(name, msg, m, arg1, arg2, argArray,
                    t);
            eventStream.publish(l);
        }
    }

    private void filterAndWarn(final String msg, final Object arg1,
            final Object arg2, final Object[] argArray, final Throwable t) {
        if (isWarnEnabled()) {
            final LogEvent l = new LogWarn(name, msg, null, arg1, arg2,
                    argArray, t);
            eventStream.publish(l);
        }
    }

    private void filterAndWarnM(final Marker m, final String msg,
            final Object arg1, final Object arg2, final Object[] argArray,
            final Throwable t) {
        if (isWarnEnabled(m)) {
            final LogEvent l = new LogWarn(name, msg, m, arg1, arg2, argArray,
                    t);
            eventStream.publish(l);
        }
    }

    private void filterAndError(final String msg, final Object arg1,
            final Object arg2, final Object[] argArray, final Throwable t) {
        if (isErrorEnabled()) {
            final LogEvent l = new LogError(name, msg, null, arg1, arg2,
                    argArray, t);
            eventStream.publish(l);
        }
    }

    private void filterAndErrorM(final Marker m, final String msg,
            final Object arg1, final Object arg2, final Object[] argArray,
            final Throwable t) {
        if (isErrorEnabled(m)) {
            final LogEvent l = new LogError(name, msg, m, arg1, arg2, argArray,
                    t);
            eventStream.publish(l);
        }
    }
}
