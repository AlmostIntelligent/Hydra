package org.gethydrated.hydra.api;

import org.gethydrated.hydra.api.service.USID;

/**
 * Hydra api.
 * @author Christian Kulpa
 * @since 0.1.0
 *
 */
public interface HydraApi {

    /**
     * Starts a service.
     * @param name service name.
     * @return unique service id.
     * @throws HydraException on start failure.
     */
    USID startService(String name) throws HydraException;

    /**
     * Stops the service with the given id.
     * @param id service id.
     * @throws HydraException on failure.
     */
    void stopService(USID id) throws HydraException;

}
