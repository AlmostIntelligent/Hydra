package org.gethydrated.hydra.api.service;

import java.io.InputStream;
import java.io.PrintStream;

import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.api.event.EventListener;
import org.gethydrated.hydra.api.event.InputEvent;

/**
 * Service context.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 */
public interface ServiceContext extends ServiceApi {

    /**
     * 
     * @return Configurations
     */
    Configuration getConfiguration();

    <T> void registerMessageHandler(Class<T> classifier, MessageHandler<T> messageHandler);

    void subscribeEvent(Class<?> classifier);
}

