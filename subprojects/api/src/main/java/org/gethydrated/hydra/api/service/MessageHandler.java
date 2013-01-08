package org.gethydrated.hydra.api.service;

/**
 *
 */
public interface MessageHandler<V> {

    void handle(V message, USID sender);

}
