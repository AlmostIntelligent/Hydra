package org.gethydrated.hydra.actors.node;

import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.actors.logging.LoggingAdapter;
import org.gethydrated.hydra.actors.refs.InternalRef;
import org.slf4j.Logger;

/**
 *
 */
public abstract class Supervisor {

    private final ActorSystem actorSystem;

    protected Supervisor(final ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }

    public void handleFailedChildren(final Throwable cause,
            final InternalRef child) throws Throwable {
        final SupervisorAction action = decide(cause);
        logFailure(cause, child, action);
        switch (action) {
        case RESTART:
            restart(cause, child);
            break;
        case RESUME:
            resume(cause, child);
            break;
        case STOP:
            stop(child);
            break;
        case ESCALATE:
            throw cause;
        default:
            break;
        }
    }

    protected void stop(final InternalRef child) {
        child.stop();
    }

    protected void restart(final Throwable cause, final InternalRef child) {
        child.restart(cause);
    }

    protected void logFailure(final Throwable cause, final InternalRef child,
            final SupervisorAction action) {
        final Logger logger = new LoggingAdapter(Supervisor.class,
                actorSystem.getEventStream());
        switch (action) {
        case RESUME:
            logger.warn("{}", child.getPath() + "" + cause.getMessage(), cause);
            break;
        case STOP:
        case RESTART:
            logger.error("{}", child.getPath() + " " + cause.getMessage(),
                    cause);
            break;
        default:
            break;
        }
    }

    protected void resume(final Throwable cause, final InternalRef child) {
        child.resume(cause);
    }

    protected abstract SupervisorAction decide(Throwable cause);

    protected enum SupervisorAction {
        RESTART, RESUME, STOP, ESCALATE
    }
}
