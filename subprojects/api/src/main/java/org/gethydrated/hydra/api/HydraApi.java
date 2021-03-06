package org.gethydrated.hydra.api;

import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.USID;

/**
 * Hydra api.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 * 
 */
public interface HydraApi {

    /**
     * Starts a service.
     * 
     * @param name
     *            service name.
     * @return unique service id.
     * @throws HydraException
     *             on start failure.
     */
    SID startService(String name) throws HydraException;

    /**
     * Returns a service with the given name.
     *
     * @param name service name.
     * @return service id.
     */
    SID getService(String name);

    /**
     * Returns a service with the given usid.
     * @param usid service usid.
     * @return service id.
     */
    SID getService(USID usid);

    /**
     * Stops the service with the given id.
     * 
     * @param id
     *            service id.
     * @throws HydraException
     *             on failure.
     */
    void stopService(SID id) throws HydraException;

}
