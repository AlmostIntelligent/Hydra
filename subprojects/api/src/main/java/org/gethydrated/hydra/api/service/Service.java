package org.gethydrated.hydra.api.service;

/**
 * Represents the service internal. The hydra platform distributes an
 * implementation. Do not implement this interface for your services!
 * 
 * @author dercikey
 * 
 */
public interface Service {

    /**
     * Attempts to start the service.
     * 
     * @throws ServiceException
     *             on errors while starting the service.
     */
    void start() throws ServiceException;

    /**
     * Attempts to stop the service.
     * 
     * @throws ServiceException
     *             on errors while stopping the service.
     */
    void stop() throws ServiceException;
    
    /**
     * Return unique service id.
     * @return service id.
     */
    Long getId();
}
