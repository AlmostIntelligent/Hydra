package org.gethydrated.hydra.actors;

import org.gethydrated.hydra.actors.refs.InternalRef;

/**
 * System messages.
 * @author Christian Kulpa
 * @since 0.2.0
 */
public final class SystemMessages {

    private SystemMessages() {
    }
    
    /**
     * System message.
     * @author Christian Kulpa
     * @since 0.2.0
     */
    public static class Create {

    }
    /**
     * System message.
     * @author Christian Kulpa
     * @since 0.2.0
     */
    public static class Stop {

    }
    /**
     * System message.
     * @author Christian Kulpa
     * @since 0.2.0
     */
    public static class Stopped {

        private final ActorPath path;

        /**
         * Constructor.
         * @param path actor path.
         */
        public Stopped(final ActorPath path) {
            this.path = path;
        }

        /**
         * Returns the actor path of the stopped actor.
         * @return actor path.
         */
        public ActorPath getPath() {
            return path;
        }

    }
    /**
     * System message.
     * @author Christian Kulpa
     * @since 0.2.0
     */
    public static class Restart {

        private final Throwable cause;

        /**
         * Constructor.
         * @param cause Suspension cause.
         */
        public Restart(final Throwable cause) {
            this.cause = cause;
        }

        /**
         * Returns the suspension cause.
         * @return suspension cause.
         */
        public Throwable getCause() {
            return cause;
        }
    }
    /**
     * System message.
     * @author Christian Kulpa
     * @since 0.2.0
     */
    public static class Suspend {
    }
    /**
     * System message.
     * @author Christian Kulpa
     * @since 0.2.0
     */
    public static class Resume {
        private final Throwable cause;
        
        /**
         * Constructor.
         * @param cause Suspension cause.
         */
        public Resume(final Throwable cause) {
            this.cause = cause;
        }

        /**
         * Returns the suspension cause.
         * @return suspension cause.
         */
        public Throwable getCause() {
            return cause;
        }
    }
    /**
     * System message.
     * @author Christian Kulpa
     * @since 0.2.0
     */
    public static class Watch {

        private final InternalRef target;

        /**
         * Constructor.
         * @param target Target actor ref.
         */
        public Watch(final InternalRef target) {
            this.target = target;
        }

        /**
         * Returns the target actor ref.
         * @return actor ref.
         */
        public InternalRef getTarget() {
            return target;
        }
    }
    /**
     * System message.
     * @author Christian Kulpa
     * @since 0.2.0
     */
    public static class UnWatch {
        private final InternalRef target;

        /**
         * Constructor.
         * @param target Target actor ref.
         */
        public UnWatch(final InternalRef target) {
            this.target = target;
        }

        /**
         * Returns the target actor ref.
         * @return actor ref.
         */
        public InternalRef getTarget() {
            return target;
        }
    }
    /**
     * System message.
     * @author Christian Kulpa
     * @since 0.2.0
     */
    public static class WatcheeStopped {
        private final InternalRef target;
        
        /**
         * Constructor.
         * @param target Target actor ref.
         */
        public WatcheeStopped(final InternalRef target) {
            this.target = target;
        }

        /**
         * Returns the target actor ref.
         * @return actor ref.
         */
        public InternalRef getTarget() {
            return target;
        }
    }
    /**
     * System message.
     * @author Christian Kulpa
     * @since 0.2.0
     */
    public static class Failed {
        private final InternalRef child;

        private final Throwable cause;

        /**
         * Constructor.
         * @param child failed child actor ref.
         * @param cause failure cause.
         */
        public Failed(final InternalRef child, final Throwable cause) {
            this.child = child;
            this.cause = cause;
        }

        /**
         * Returns the failed child ref.
         * @return child ref.
         */
        public InternalRef getChild() {
            return child;
        }

        /**
         * Returns the failure cause.
         * @return failure cause.
         */
        public Throwable getCause() {
            return cause;
        }
    }
}
