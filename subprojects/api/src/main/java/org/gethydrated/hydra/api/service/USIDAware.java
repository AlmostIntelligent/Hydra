package org.gethydrated.hydra.api.service;

/**
 * Marker interface for USID aware classes.
 * @author Christian Kulpa.
 * @since 0.2.0
 */
public interface USIDAware {

    /**
     * Returns the service usid.
     * @return service usid.
     */
    USID getUSID();

}
