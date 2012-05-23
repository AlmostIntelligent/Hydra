package org.gethydrated.hydra.api.service;

import java.io.InputStream;
import java.io.PrintStream;

import org.gethydrated.hydra.api.configuration.ConfigurationGetter;
import org.gethydrated.hydra.api.configuration.ConfigurationSetter;

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

    PrintStream getOutputStream();

    InputStream getInputStream();

    ConfigurationGetter getConfigurationGetter();

    ConfigurationSetter getConfigurationSetter();
}
