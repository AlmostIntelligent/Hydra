package org.gethydrated.hydra.actors.node;

import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.actors.error.ActorInitialisationException;

/**
 *
 */
public class DefaultSupervisor extends Supervisor {
    public DefaultSupervisor(ActorSystem actorSystem) {
        super(actorSystem);
    }

    @Override
    protected SupervisorAction decide(Throwable cause) {
        if(cause instanceof ActorInitialisationException) {
            return SupervisorAction.STOP;
        } else if (cause instanceof Exception) {
            return SupervisorAction.RESTART;
        }
        return SupervisorAction.ESCALATE;
    }
}
