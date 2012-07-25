package org.gethydrated.hydra.api.message;

/**
 * Defines a (message) object as sendable over the network.
 * 
 * @author Hanno Sternberg
 *
 */
public interface Sendable extends Message {

    /**
     * 
     * @return Object Data in correct format.      
     */
    String generateProtocollString();
}
