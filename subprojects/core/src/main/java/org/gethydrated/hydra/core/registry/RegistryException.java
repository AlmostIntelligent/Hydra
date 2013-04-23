package org.gethydrated.hydra.core.registry;

import org.gethydrated.hydra.api.HydraException;

/**
 * Registry exception.
 */
public class RegistryException extends HydraException {

    private static final long serialVersionUID = 1737024740147572279L;

    /**
     * Constructor.
     * @param e Cause.
     */
    public RegistryException(final Throwable e) {
        super(e);
    }

    /**
     * Constructor.
     * @param m Message.
     */
    public RegistryException(final String m) {
        super(m);
    }
}
