package org.gethydrated.hydra.core.service.locator;

import java.net.URL;

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
     */
    URL locate(String name);
    
    /**
     * Tries to locate a service.
     * @param name Service name.
     * @param version Serive version.
     * @return URL to service jar file.
     */
    URL locate(String name, String version);
}
