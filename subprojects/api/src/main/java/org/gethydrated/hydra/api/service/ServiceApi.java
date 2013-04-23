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
     * 
     * @param name
     *            service name.
     * @param id
     *            service id.
     * @throws HydraException
     *            on failure.
     */
    void registerLocal(String name, SID id) throws HydraException;

    /**
     * Registers a service to global name resolution.
     * 
     * @param name
     *            service name.
     * @param id
     *            service id.
     * @throws HydraException
     *            on failure.
     */
    void registerGlobal(String name, SID id) throws HydraException;

    /**
     * Unregisters a service to local name resolution.
     * 
     * @param name
     *            service name.
     * @throws HydraException
     *            on failure.
     */
    void unregisterLocal(String name) throws HydraException;

    /**
     * Unregisters a service to global name resolution.
     * 
     * @param name
     *            service name.
     * @throws HydraException
     *            on failure.
     */
    void unregisterGlobal(String name) throws HydraException;

    /**
     * Requests a local service.
     * 
     * @param name
     *            service name.
     * @return service id.
     * @throws HydraException
     *            on failure.
     */
    SID getLocalService(String name) throws HydraException;

    /**
     * Requests a global service.
     * 
     * @param name
     *            service name.
     * @return service id.
     * @throws HydraException
     *            on failure.
     */
    SID getGlobalService(String name) throws HydraException;

    /**
     * Returns a service id factory.
     * @return service id factory.
     */
    SIDFactory getSIDFactory();

    /**
     * Links two service ids together.
     * @param sid1 service 1.
     * @param sid2 service 2.
     */
    void link(SID sid1, SID sid2);

    /**
     * Unlinks two service ids.
     * @param sid1 service 1.
     * @param sid2 service 2.
     */
    void unlink(SID sid1, SID sid2);

    /**
     * Lets a service monitor an other service.
     * @param sid1 monitoring service.
     * @param sid2 monitored service.
     */
    void monitor(SID sid1, SID sid2);

    /**
     * Stops monitoring between services.
     * @param sid1 monitoring service.
     * @param sid2 monitored service.
     */
    void unmonitor(SID sid1, SID sid2);
}
