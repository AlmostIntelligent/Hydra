package org.gethydrated.hydra.actors.error;

/**
 * ActorInitialisationException.
 */
public class ActorInitialisationException extends ActorException {
    /**
     * Constructor.
     */
    private static final long serialVersionUID = -6377012519811026665L;

    /**
     * Constructor.
     * @param e Cause
     */
    public ActorInitialisationException(final Throwable e) {
        super(e);
    }
}
