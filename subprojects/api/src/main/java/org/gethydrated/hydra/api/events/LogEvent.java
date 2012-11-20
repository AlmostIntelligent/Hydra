package org.gethydrated.hydra.api.events;

import org.slf4j.Logger;
import org.slf4j.MDC;
import org.slf4j.Marker;

public abstract class LogEvent {

	protected final String threadname = Thread.currentThread().getName();
	
	protected final String source;

	protected final Marker marker;

	protected final String message;

	protected final Object arg1;

	protected final Object arg2;

	protected final Object[] argArray;

	protected final Throwable throwable;

	public LogEvent(String source, String msg, Marker m, Object arg1, Object arg2,
			Object[] argArray, Throwable t) {
		this.source = source;
		message = msg;
		marker = m;
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.argArray = argArray;
		this.throwable = t;
	}

	public String getMessage() {
		return message;
	}
	
	public String getSource() {
		return source;
	}
	
	public void logInto(Logger logger) {
		setMDC();
		log(logger);
		MDC.clear();
	}

	protected abstract void log(Logger logger);
	
	private void setMDC() {
		MDC.put("sourceThread", threadname);
		MDC.put("source", source);
	}
	
	public static class LogError extends LogEvent {

		public LogError(String source, String msg, Marker m, Object arg1, Object arg2,
				Object[] argArray, Throwable t) {
			super(source, msg, m, arg1, arg2, argArray, t);
		}

		@Override
		protected void log(Logger logger) {
			if (marker != null) {
				if (arg2 != null) {
					logger.error(marker, message, arg1, arg2);
				} else if (arg1 != null) {
					logger.error(marker, message, arg1);
				} else if (argArray != null) {
					logger.error(marker, message, argArray);
				} else if (throwable != null) {
					logger.error(marker, message, throwable);
				} else {
					logger.error(marker, message);
				}
			} else {
				if (arg2 != null) {
					logger.error(message, arg1, arg2);
				} else if (arg1 != null) {
					logger.error(message, arg1);
				} else if (argArray != null) {
					logger.error(message, argArray);
				} else if (throwable != null) {
					logger.error(message, throwable);
				} else {
					logger.error(message);
				}
			}
		}

	}

	public static class LogWarn extends LogEvent {

		public LogWarn(String source, String msg, Marker m, Object arg1, Object arg2,
				Object[] argArray, Throwable t) {
			super(source, msg, m, arg1, arg2, argArray, t);
		}

		@Override
		protected void log(Logger logger) {
			if (marker != null) {
				if (arg2 != null) {
					logger.warn(marker, message, arg1, arg2);
				} else if (arg1 != null) {
					logger.warn(marker, message, arg1);
				} else if (argArray != null) {
					logger.warn(marker, message, argArray);
				} else if (throwable != null) {
					logger.warn(marker, message, throwable);
				} else {
					logger.warn(marker, message);
				}
			} else {
				if (arg2 != null) {
					logger.warn(message, arg1, arg2);
				} else if (arg1 != null) {
					logger.warn(message, arg1);
				} else if (argArray != null) {
					logger.warn(message, argArray);
				} else if (throwable != null) {
					logger.warn(message, throwable);
				} else {
					logger.warn(message);
				}
			}
		}

	}

	public static class LogInfo extends LogEvent {

		public LogInfo(String source, String msg, Marker m, Object arg1, Object arg2,
				Object[] argArray, Throwable t) {
			super(source, msg, m, arg1, arg2, argArray, t);
		}

		@Override
		protected void log(Logger logger) {
			if (marker != null) {
				if (arg2 != null) {
					logger.info(marker, message, arg1, arg2);
				} else if (arg1 != null) {
					logger.info(marker, message, arg1);
				} else if (argArray != null) {
					logger.info(marker, message, argArray);
				} else if (throwable != null) {
					logger.info(marker, message, throwable);
				} else {
					logger.info(marker, message);
				}
			} else {
				if (arg2 != null) {
					logger.info(message, arg1, arg2);
				} else if (arg1 != null) {
					logger.info(message, arg1);
				} else if (argArray != null) {
					logger.info(message, argArray);
				} else if (throwable != null) {
					logger.info(message, throwable);
				} else {
					logger.info(message);
				}
			}
		}

	}

	public static class LogDebug extends LogEvent {

		public LogDebug(String source, String msg, Marker m, Object arg1, Object arg2,
				Object[] argArray, Throwable t) {
			super(source, msg, m, arg1, arg2, argArray, t);
		}

		@Override
		protected void log(Logger logger) {
			if (marker != null) {
				if (arg2 != null) {
					logger.debug(marker, message, arg1, arg2);
				} else if (arg1 != null) {
					logger.debug(marker, message, arg1);
				} else if (argArray != null) {
					logger.debug(marker, message, argArray);
				} else if (throwable != null) {
					logger.debug(marker, message, throwable);
				} else {
					logger.debug(marker, message);
				}
			} else {
				if (arg2 != null) {
					logger.debug(message, arg1, arg2);
				} else if (arg1 != null) {
					logger.debug(message, arg1);
				} else if (argArray != null) {
					logger.debug(message, argArray);
				} else if (throwable != null) {
					logger.debug(message, throwable);
				} else {
					logger.debug(message);
				}
			}
		}

	}

	public static class LogTrace extends LogEvent {

		public LogTrace(String source, String msg, Marker m, Object arg1, Object arg2,
				Object[] argArray, Throwable t) {
			super(source, msg, m, arg1, arg2, argArray, t);
		}

		@Override
		protected void log(Logger logger) {
			if (marker != null) {
				if (arg2 != null) {
					logger.trace(marker, message, arg1, arg2);
				} else if (arg1 != null) {
					logger.trace(marker, message, arg1);
				} else if (argArray != null) {
					logger.trace(marker, message, argArray);
				} else if (throwable != null) {
					logger.trace(marker, message, throwable);
				} else {
					logger.trace(marker, message);
				}
			} else {
				if (arg2 != null) {
					logger.trace(message, arg1, arg2);
				} else if (arg1 != null) {
					logger.trace(message, arg1);
				} else if (argArray != null) {
					logger.trace(message, argArray);
				} else if (throwable != null) {
					logger.trace(message, throwable);
				} else {
					logger.trace(message);
				}
			}
		}

	}
}
