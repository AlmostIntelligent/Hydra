package org.gethydrated.hydra.api.service;

/**
 * Service activator interface.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 *
 */
public interface ServiceActivator {
    
    /**
     * On start.
     * @param context service context.
     * @throws Exception on failure.
     */
    void start(ServiceContext context) throws Exception;

    /**
     * On stop.
     * @param context service context
     * @throws Exception on failure.
     */
    void stop(ServiceContext context) throws Exception;
}
