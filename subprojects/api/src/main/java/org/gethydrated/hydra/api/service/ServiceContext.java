package org.gethydrated.hydra.api.service;

import java.io.InputStream;
import java.io.PrintStream;

import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.api.event.EventListener;

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

    /**
     * 
     * @return Configurations
     */
    Configuration getConfiguration();

}

