package org.gethydrated.hydra.api.service;

import java.io.InputStream;
import java.io.PrintStream;

import org.gethydrated.hydra.api.configuration.Configuration;

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
     * Returns system output stream.
     * @return PrintStream.
     */
    PrintStream getOutputStream();

    /**
     * Returns system input stream.
     * @return InputStream.
     */
    InputStream getInputStream();

    /**
     * 
     * @return Configurations
     */
    Configuration getConfiguration();
}

