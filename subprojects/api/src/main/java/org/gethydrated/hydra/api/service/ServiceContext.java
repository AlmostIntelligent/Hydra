package org.gethydrated.hydra.api.service;

import org.gethydrated.hydra.api.configuration.Configuration;

/**
 * Service context.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 */
public interface ServiceContext extends ServiceApi {

    SID getSelf();

    SID getOutput();

    /**
     * 
     * @return Configurations
     */
    Configuration getConfiguration();

    <T> void registerMessageHandler(Class<T> classifier, MessageHandler<T> messageHandler);

    void subscribeEvent(Class<?> classifier);
}

