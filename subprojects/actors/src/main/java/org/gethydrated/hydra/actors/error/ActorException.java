package org.gethydrated.hydra.actors.error;

/**
 * ActorException.
 */
public class ActorException extends Exception {

    private static final long serialVersionUID = -6329687891719386181L;

    /**
     * Constructor.
     * @param e Cause
     */
    public ActorException(final Throwable e) {
        super(e);
    }
}
