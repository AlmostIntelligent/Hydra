package org.gethydrated.hydra.core.service;

import org.gethydrated.hydra.core.config.Configuration;
import org.gethydrated.hydra.core.service.locator.ServiceLocator;
import org.gethydrated.hydra.core.service.locator.SystemServiceLocator;

/**
 * Manages running services.
 * @author Christian Kulpa
 * @since 0.1.0
 *
 */
public class ServiceManager {

    private final ServiceLocator sl;
    
    public ServiceManager(Configuration cfg) {
        sl = new SystemServiceLocator(cfg);
    }
    
    public void startService(String name) {
        
    }
    

}
