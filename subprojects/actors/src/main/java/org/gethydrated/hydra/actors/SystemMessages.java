package org.gethydrated.hydra.actors;


public class SystemMessages {

    private SystemMessages() {
    }

    public static class Start {

    }

    public static class Stop {

    }

    public static class Restart {

    }

    public static class Pause {

    }

    public static class Resume {

    }

    public static class Watch {

        private final ActorRef target;

        public Watch(ActorRef target) {
            this.target = target;
        }

        public ActorRef getTarget() {
            return target;
        }
    }

    public static class UnWatch {
        private final ActorRef target;

        public UnWatch(ActorRef target) {
            this.target = target;
        }

        public ActorRef getTarget() {
            return target;
        }
    }

    public static class Stopped {
        private final ActorRef target;

        public Stopped(ActorRef target) {
            this.target = target;
        }

        public ActorRef getTarget() {
            return target;
        }
    }
}
