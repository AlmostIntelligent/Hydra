package org.gethydrated.hydra.api;

import org.gethydrated.hydra.api.service.SID;

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
    SID startService(String name) throws HydraException;

    /**
     * Stops the service with the given id.
     * @param id service id.
     * @throws HydraException on failure.
     */
    void stopService(SID id) throws HydraException;

}
