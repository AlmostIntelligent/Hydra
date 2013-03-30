package org.gethydrated.hydra.actors;


import org.gethydrated.hydra.actors.refs.InternalRef;

public class SystemMessages {

    private SystemMessages() {
    }

    public static class Create {

    }

    public static class Stop {

    }

    public static class Stopped {

        private ActorPath path;

        public Stopped(ActorPath path) {
            this.path = path;
        }

        public ActorPath getPath() {
            return path;
        }

    }

    public static class Restart {

        private final Throwable cause;

        public Restart(Throwable cause) {
            this.cause = cause;
        }

        public Throwable getCause() {
            return cause;
        }
    }

    public static class Suspend {
    }

    public static class Resume {
        private final Throwable cause;

        public Resume(Throwable cause) {
            this.cause = cause;
        }

        public Throwable getCause() {
            return cause;
        }
    }

    public static class Watch {

        private final InternalRef target;

        public Watch(InternalRef target) {
            this.target = target;
        }

        public InternalRef getTarget() {
            return target;
        }
    }

    public static class UnWatch {
        private final InternalRef target;

        public UnWatch(InternalRef target) {
            this.target = target;
        }

        public InternalRef getTarget() {
            return target;
        }
    }

    public static class WatcheeStopped {
        private final InternalRef target;

        public WatcheeStopped(InternalRef target) {
            this.target = target;
        }

        public InternalRef getTarget() {
            return target;
        }
    }

    public static class Failed {
        private final InternalRef child;

        private final Throwable cause;

        public Failed(InternalRef child, Throwable cause) {
            this.child = child;
            this.cause = cause;
        }

        public InternalRef getChild() {
            return child;
        }

        public Throwable getCause() {
            return cause;
        }
    }
}
