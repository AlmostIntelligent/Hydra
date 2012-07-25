package org.gethydrated.hydra.api.message;

import org.gethydrated.hydra.api.service.USID;

/**
 * Message Interface.
 * 
 * @author Hanno Sternberg
 *
 */
public interface Message {
    
    /**
     * 
     * @return Type of the message.
     */
    MessageType getType();
    
    /**
     * 
     * @return Destination of the message.
     */
    USID getDestination();
    
    /**
     * 
     * @return Source of the message.
     */
    USID getSource();
}
