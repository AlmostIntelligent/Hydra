package org.gethydrated.hydra.api.event;

import org.slf4j.Logger;
import org.slf4j.MDC;
import org.slf4j.Marker;

/**
 * Log events.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 */
public abstract class LogEvent implements SystemEvent {

    protected final String threadname = Thread.currentThread().getName();

    protected final String source;

    protected final Marker marker;

    protected final String message;

    protected final Object arg1;

    protected final Object arg2;

    protected final Object[] argArray;

    protected final Throwable throwable;

    /**
     * Constructor.
     * @param source event source.
     * @param msg event message.
     * @param m event marker.
     * @param arg1 event arg1.
     * @param arg2 event arg2.
     * @param argArray event argArray.
     * @param t event cause.
     */
    public LogEvent(final String source, final String msg, final Marker m,
            final Object arg1, final Object arg2, final Object[] argArray,
            final Throwable t) {
        this.source = source;
        message = msg;
        marker = m;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.argArray = argArray;
        this.throwable = t;
    }

    /**
     * Returns the event message.
     * @return event message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the event source.
     * @return event source.
     */
    public String getSource() {
        return source;
    }

    /**
     * Logs the event with the given logger.
     * @param logger used logger.
     */
    public void logInto(final Logger logger) {
        setMDC();
        log(logger);
        MDC.clear();
    }

    /**
     * Logs the event with the given logger.
     * @param logger used logger.
     */
    protected abstract void log(Logger logger);

    private void setMDC() {
        MDC.put("sourceThread", threadname);
        MDC.put("source", source);
    }

    /**
     * Error level log event.
     * @author Christian Kulpa
     * @since 0.2.0
     */
    public static class LogError extends LogEvent {

        /**
         * Constructor.
         * @param source event source.
         * @param msg event message.
         * @param m event marker.
         * @param arg1 event arg1.
         * @param arg2 event arg2.
         * @param argArray event argArray.
         * @param t event cause.
         */
        public LogError(final String source, final String msg, final Marker m,
                final Object arg1, final Object arg2, final Object[] argArray,
                final Throwable t) {
            super(source, msg, m, arg1, arg2, argArray, t);
        }

        @Override
        protected void log(final Logger logger) {
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

    /**
     * Warn level log event.
     * @author Christian Kulpa
     * @since 0.2.0
     */
    public static class LogWarn extends LogEvent {

        /**
         * Constructor.
         * @param source event source.
         * @param msg event message.
         * @param m event marker.
         * @param arg1 event arg1.
         * @param arg2 event arg2.
         * @param argArray event argArray.
         * @param t event cause.
         */
        public LogWarn(final String source, final String msg, final Marker m,
                final Object arg1, final Object arg2, final Object[] argArray,
                final Throwable t) {
            super(source, msg, m, arg1, arg2, argArray, t);
        }

        @Override
        protected void log(final Logger logger) {
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

    /**
     * Info level log event.
     * @author Christian Kulpa
     * @since 0.2.0
     */
    public static class LogInfo extends LogEvent {

        /**
         * Constructor.
         * @param source event source.
         * @param msg event message.
         * @param m event marker.
         * @param arg1 event arg1.
         * @param arg2 event arg2.
         * @param argArray event argArray.
         * @param t event cause.
         */
        public LogInfo(final String source, final String msg, final Marker m,
                final Object arg1, final Object arg2, final Object[] argArray,
                final Throwable t) {
            super(source, msg, m, arg1, arg2, argArray, t);
        }

        @Override
        protected void log(final Logger logger) {
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

    /**
     * Debug level log event.
     * @author Christian Kulpa
     * @since 0.2.0
     */
    public static class LogDebug extends LogEvent {

        /**
         * Constructor.
         * @param source event source.
         * @param msg event message.
         * @param m event marker.
         * @param arg1 event arg1.
         * @param arg2 event arg2.
         * @param argArray event argArray.
         * @param t event cause.
         */
        public LogDebug(final String source, final String msg, final Marker m,
                final Object arg1, final Object arg2, final Object[] argArray,
                final Throwable t) {
            super(source, msg, m, arg1, arg2, argArray, t);
        }

        @Override
        protected void log(final Logger logger) {
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

    /**
     * Trace level log event.
     * @author Christian Kulpa
     * @since 0.2.0
     */
    public static class LogTrace extends LogEvent {

        /**
         * Constructor.
         * @param source event source.
         * @param msg event message.
         * @param m event marker.
         * @param arg1 event arg1.
         * @param arg2 event arg2.
         * @param argArray event argArray.
         * @param t event cause.
         */
        public LogTrace(final String source, final String msg, final Marker m,
                final Object arg1, final Object arg2, final Object[] argArray,
                final Throwable t) {
            super(source, msg, m, arg1, arg2, argArray, t);
        }

        @Override
        protected void log(final Logger logger) {
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
