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

    }

    public static class Pause {

    }

    public static class Resume {

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
}
