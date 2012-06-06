package org.gethydrated.hydra.api.service;

import org.gethydrated.hydra.api.HydraException;

/**
 * Hydra service exception.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 *
 */
public class ServiceException extends HydraException {

    /**
     * Constructor.
     * @param e Throwable e.
     */
    public ServiceException(final Throwable e) {
        super(e);
    }

    /**
     * Constructor.
     * @param m Message m.
     */
    public ServiceException(final String m) {
        super(m);
    }

    /**
     * Serialization id.
     */
    private static final long serialVersionUID = -3496130502175923936L;

}
