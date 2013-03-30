package org.gethydrated.hydra.api.service;

import org.gethydrated.hydra.api.HydraApi;
import org.gethydrated.hydra.api.HydraException;

/**
 * Service related api.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 *
 */
public interface ServiceApi extends HydraApi {

    /**
     * Registers a service to local name resolution.
     * @param name service name.
     * @param id service id.
     */
    void registerLocal(String name, SID id) throws HydraException;

    /**
     * Registers a service to global name resolution.
     * @param name service name.
     * @param id service id.
     */
    void registerGlobal(String name, SID id) throws HydraException;

    /**
     * Unregisters a service to local name resolution.
     * @param name service name.
     */
    void unregisterLocal(String name) throws HydraException;

    /**
     * Unregisters a service to global name resolution.
     * @param name service name.
     */
    void unregisterGlobal(String name) throws HydraException;

    /**
     * Requests a local service.
     * @param name service name.
     * @return TODO
     */
    SID getLocalService(String name) throws HydraException;

    /**
     * Requests a global service.
     * @param name service name.
     * @return TODO
     */
    SID getGlobalService(String name) throws HydraException;

    SIDFactory getSIDFactory();

    void link(SID sid);

    void unlink(SID sid);

    void monitor(SID sid);

    void unmonitor(SID sid);
}
