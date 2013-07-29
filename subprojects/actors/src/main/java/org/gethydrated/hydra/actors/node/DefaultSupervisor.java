package org.gethydrated.hydra.actors.node;

import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.actors.error.ActorInitialisationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class DefaultSupervisor extends Supervisor {

    private final Logger logger = LoggerFactory.getLogger(DefaultSupervisor.class);

    public DefaultSupervisor(final ActorSystem actorSystem) {
        super(actorSystem);
    }

    @Override
    protected SupervisorAction decide(final Throwable cause) {
        logger.debug("{}",cause);
        if (cause instanceof ActorInitialisationException) {
            return SupervisorAction.STOP;
        } else if (cause instanceof Exception) {
            return SupervisorAction.RESTART;
        }
        return SupervisorAction.ESCALATE;
    }
}
