package org.gethydrated.hydra.api.service;

import org.gethydrated.hydra.api.configuration.Configuration;

/**
 * Service context.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 */
public interface ServiceContext extends ServiceApi {

    /**
     * Returns the own service id.
     * @return own service id.
     */
    SID getSelf();

    /**
     * Returns a service id to the standard output
     * actor.
     * @return output actor id.
     */
    SID getOutput();

    /**
     * 
     * @return Configurations
     */
    Configuration getConfiguration();

    /**
     * Registers a message handler.
     * @param classifier message classifier.
     * @param messageHandler message handler.
     * @param <T> Result type.
     */
    <T> void registerMessageHandler(Class<T> classifier,
            MessageHandler<T> messageHandler);

    /**
     * Subscribes the classifier to eventstream.
     * @param classifier .
     */
    void subscribeEvent(Class<?> classifier);
}
