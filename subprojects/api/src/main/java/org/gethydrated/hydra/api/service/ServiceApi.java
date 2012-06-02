package org.gethydrated.hydra.api.service;

import org.gethydrated.hydra.api.HydraApi;

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
    void registerLocal(String name, Long id);

    /**
     * Registers a service to global name resolution.
     * @param name service name.
     * @param id service id.
     */
    void registerGlobal(String name, Long id);

    /**
     * Requests a local service.
     * @param name service name.
     * @return TODO
     */
    Long getLocalService(String name);

    /**
     * Requests a global service.
     * @param name service name.
     * @return TODO
     */
    Long getGlobalService(String name);
}
