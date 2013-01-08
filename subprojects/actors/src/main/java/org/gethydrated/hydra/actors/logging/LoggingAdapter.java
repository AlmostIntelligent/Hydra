package org.gethydrated.hydra.actors.logging;

import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.api.event.EventStream;
import org.gethydrated.hydra.api.event.LogEvent;
import org.gethydrated.hydra.api.event.LogEvent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

public class LoggingAdapter implements Logger {

	private final String name;

	private final EventStream eventStream;

	private final Logger logger;

	public LoggingAdapter(Class<?> clazz, ActorSystem system) {
		this(clazz.getName(), system);
	}

	public LoggingAdapter(String name, ActorSystem system) {
		this.name = name;
		logger = LoggerFactory.getLogger(name);
		eventStream = system.getEventStream();
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
	public void trace(String msg) {
		filterAndTrace(msg, null, null, null, null);
	}

	@Override
	public void trace(String format, Object arg) {
		filterAndTrace(format, arg, null, null, null);
	}

	@Override
	public void trace(String format, Object arg1, Object arg2) {
		filterAndTrace(format, arg1, arg2, null, null);
	}

	@Override
	public void trace(String format, Object[] argArray) {
		filterAndTrace(format, null, null, argArray, null);
	}

	@Override
	public void trace(String msg, Throwable t) {
		filterAndTrace(msg, null, null, null, t);
	}

	@Override
	public boolean isTraceEnabled(Marker marker) {
		return logger.isTraceEnabled(marker);
	}

	@Override
	public void trace(Marker marker, String msg) {
		filterAndTraceM(marker, msg, null, null, null, null);
	}

	@Override
	public void trace(Marker marker, String format, Object arg) {
		filterAndTraceM(marker, format, arg, null, null, null);

	}

	@Override
	public void trace(Marker marker, String format, Object arg1, Object arg2) {
		filterAndTraceM(marker, format, arg1, arg2, null, null);

	}

	@Override
	public void trace(Marker marker, String format, Object[] argArray) {
		filterAndTraceM(marker, format, null, null, argArray, null);

	}

	@Override
	public void trace(Marker marker, String msg, Throwable t) {
		filterAndTraceM(marker, msg, null, null, null, t);

	}

	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public void debug(String msg) {
		filterAndDebug(msg, null, null, null, null);
	}

	@Override
	public void debug(String format, Object arg) {
		filterAndDebug(format, arg, null, null, null);

	}

	@Override
	public void debug(String format, Object arg1, Object arg2) {
		filterAndDebug(format, arg1, arg2, null, null);

	}

	@Override
	public void debug(String format, Object[] argArray) {
		filterAndDebug(format, null, null, argArray, null);

	}

	@Override
	public void debug(String msg, Throwable t) {
		filterAndDebug(msg, null, null, null, t);
	}

	@Override
	public boolean isDebugEnabled(Marker marker) {
		return logger.isDebugEnabled(marker);
	}

	@Override
	public void debug(Marker marker, String msg) {
		filterAndDebugM(marker, msg, null, null, null, null);

	}

	@Override
	public void debug(Marker marker, String format, Object arg) {
		filterAndDebugM(marker, format, arg, null, null, null);
	}

	@Override
	public void debug(Marker marker, String format, Object arg1, Object arg2) {
		filterAndDebugM(marker, format, arg1, arg2, null, null);
	}

	@Override
	public void debug(Marker marker, String format, Object[] argArray) {
		filterAndDebugM(marker, format, null, null, argArray, null);
	}

	@Override
	public void debug(Marker marker, String msg, Throwable t) {
		filterAndDebugM(marker, msg, null, null, null, t);
	}

	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public void info(String msg) {
		filterAndInfo(msg, null, null, null, null);
	}

	@Override
	public void info(String format, Object arg) {
		filterAndInfo(format, arg, null, null, null);
	}

	@Override
	public void info(String format, Object arg1, Object arg2) {
		filterAndInfo(format, arg1, arg2, null, null);
	}

	@Override
	public void info(String format, Object[] argArray) {
		filterAndInfo(format, null, null, argArray, null);
	}

	@Override
	public void info(String msg, Throwable t) {
		filterAndInfo(msg, null, null, null, t);
	}

	@Override
	public boolean isInfoEnabled(Marker marker) {
		return logger.isInfoEnabled(marker);
	}

	@Override
	public void info(Marker marker, String msg) {
		filterAndInfoM(marker, msg, null, null, null, null);
	}

	@Override
	public void info(Marker marker, String format, Object arg) {
		filterAndInfoM(marker, format, arg, null, null, null);
	}

	@Override
	public void info(Marker marker, String format, Object arg1, Object arg2) {
		filterAndInfoM(marker, format, arg1, arg2, null, null);
	}

	@Override
	public void info(Marker marker, String format, Object[] argArray) {
		filterAndInfoM(marker, format, null, null, argArray, null);
	}

	@Override
	public void info(Marker marker, String msg, Throwable t) {
		filterAndInfoM(marker, msg, null, null, null, t);
	}

	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	@Override
	public void warn(String msg) {
		filterAndWarn(msg, null, null, null, null);
	}

	@Override
	public void warn(String format, Object arg) {
		filterAndWarn(format, arg, null, null, null);
	}

	@Override
	public void warn(String format, Object[] argArray) {
		filterAndWarn(format, null, null, argArray, null);
	}

	@Override
	public void warn(String format, Object arg1, Object arg2) {
		filterAndWarn(format, arg1, arg2, null, null);
	}

	@Override
	public void warn(String msg, Throwable t) {
		filterAndWarn(msg, null, null, null, t);
	}

	@Override
	public boolean isWarnEnabled(Marker marker) {
		return logger.isWarnEnabled(marker);
	}

	@Override
	public void warn(Marker marker, String msg) {
		filterAndWarnM(marker, msg, null, null, null, null);
	}

	@Override
	public void warn(Marker marker, String format, Object arg) {
		filterAndWarnM(marker, format, arg, null, null, null);
	}

	@Override
	public void warn(Marker marker, String format, Object arg1, Object arg2) {
		filterAndWarnM(marker, format, arg1, arg2, null, null);
	}

	@Override
	public void warn(Marker marker, String format, Object[] argArray) {
		filterAndWarnM(marker, format, null, null, argArray, null);
	}

	@Override
	public void warn(Marker marker, String msg, Throwable t) {
		filterAndWarnM(marker, msg, null, null, null, t);
	}

	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public void error(String msg) {
		filterAndError(msg, null, null, null, null);
	}

	@Override
	public void error(String format, Object arg) {
		filterAndError(format, arg, null, null, null);
	}

	@Override
	public void error(String format, Object arg1, Object arg2) {
		filterAndError(format, arg1, arg2, null, null);
	}

	@Override
	public void error(String format, Object[] argArray) {
		filterAndError(format, null, null, argArray, null);
	}

	@Override
	public void error(String msg, Throwable t) {
		filterAndError(msg, null, null, null, t);
	}

	@Override
	public boolean isErrorEnabled(Marker marker) {
		return logger.isErrorEnabled(marker);
	}

	@Override
	public void error(Marker marker, String msg) {
		filterAndErrorM(marker, msg, null, null, null, null);
	}

	@Override
	public void error(Marker marker, String format, Object arg) {
		filterAndErrorM(marker, format, arg, null, null, null);
	}

	@Override
	public void error(Marker marker, String format, Object arg1, Object arg2) {
		filterAndErrorM(marker, format, arg1, arg2, null, null);
	}

	@Override
	public void error(Marker marker, String format, Object[] argArray) {
		filterAndErrorM(marker, format, null, null, argArray, null);
	}

	@Override
	public void error(Marker marker, String msg, Throwable t) {
		filterAndErrorM(marker, msg, null, null, null, t);
	}

	private void filterAndTrace(String msg, Object arg1, Object arg2,
			Object[] argArray, Throwable t) {
		if(isTraceEnabled()) {
			LogTrace l = new LogTrace(name, msg, null, arg1, arg2, argArray, t);
			eventStream.publish(l);
		}
	}

	private void filterAndTraceM(Marker m, String msg, Object arg1,
			Object arg2, Object[] argArray, Throwable t) {
		if(isTraceEnabled(m)) {
			LogEvent l = new LogTrace(name, msg, m, arg1, arg2, argArray, t);
			eventStream.publish(l);
		}
	}

	private void filterAndDebug(String msg, Object arg1, Object arg2,
			Object[] argArray, Throwable t) {
		if(isDebugEnabled()) {
			LogEvent l = new LogDebug(name, msg, null, arg1, arg2, argArray, t);
			eventStream.publish(l);
		}
	}

	private void filterAndDebugM(Marker m, String msg, Object arg1,
			Object arg2, Object[] argArray, Throwable t) {
		if(isDebugEnabled(m)) {
			LogEvent l = new LogDebug(name, msg, m, arg1, arg2, argArray, t);
			eventStream.publish(l);
		}
	}

	private void filterAndInfo(String msg, Object arg1, Object arg2,
			Object[] argArray, Throwable t) {
		if(isInfoEnabled()) {
			LogEvent l = new LogInfo(name, msg, null, arg1, arg2, argArray, t);
			eventStream.publish(l);
		}
	}

	private void filterAndInfoM(Marker m, String msg, Object arg1, Object arg2,
			Object[] argArray, Throwable t) {
		if(isInfoEnabled(m)) {
			LogEvent l = new LogInfo(name, msg, m, arg1, arg2, argArray, t);
			eventStream.publish(l);
		}
	}

	private void filterAndWarn(String msg, Object arg1, Object arg2,
			Object[] argArray, Throwable t) {
		if(isWarnEnabled()) {
			LogEvent l = new LogWarn(name, msg, null, arg1, arg2, argArray, t);
			eventStream.publish(l);
		}
	}

	private void filterAndWarnM(Marker m, String msg, Object arg1, Object arg2,
			Object[] argArray, Throwable t) {
		if(isWarnEnabled(m)) {
			LogEvent l = new LogWarn(name, msg, m, arg1, arg2, argArray, t);
			eventStream.publish(l);
		}
	}

	private void filterAndError(String msg, Object arg1, Object arg2,
			Object[] argArray, Throwable t) {
		if(isErrorEnabled()) {
			LogEvent l = new LogError(name, msg, null, arg1, arg2, argArray, t);
			eventStream.publish(l);
		}
	}

	private void filterAndErrorM(Marker m, String msg, Object arg1,
			Object arg2, Object[] argArray, Throwable t) {
		if(isErrorEnabled(m)) {
			LogEvent l = new LogError(name, msg, m, arg1, arg2, argArray, t);
			eventStream.publish(l);
		}
	}
}
