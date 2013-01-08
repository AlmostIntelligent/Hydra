package org.gethydrated.hydra.api.service;

/**
 * Represents the service internal. The hydra platform distributes an
 * implementation. Do not implement this interface for your services!
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 */
public interface Service {

    <T> void addMessageHandler(Class<T> classifier, MessageHandler<T> messageHandler);
}
