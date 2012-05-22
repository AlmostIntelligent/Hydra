package org.gethydrated.hydra.core.service.locator;

import java.io.IOException;
import org.gethydrated.hydra.core.service.ServiceInfo;

/**
 * 
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 * 
 */
public interface ServiceLocator {
    /**
     * Tries to locate a service by name.
     * @param name Service name.
     * @return URL to service jar file.
     * @throws IOException 
     */
    ServiceInfo locate(String name) throws IOException;
    
    /**
     * Tries to locate a service.
     * @param name Service name.
     * @param version Serive version.
     * @return URL to service jar file.
     * @throws IOException 
     */
    ServiceInfo locate(String name, String version) throws IOException;
}
