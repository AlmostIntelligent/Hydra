package org.gethydrated.hydra.api.service;

/**
 * Service context.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 */
public interface ServiceContext extends ServiceApi {

    /**
     * Returns the corresponding service instance.
     * 
     * @return service instance
     */
    Service getService();
}
