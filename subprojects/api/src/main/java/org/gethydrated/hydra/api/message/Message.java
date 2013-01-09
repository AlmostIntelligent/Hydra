package org.gethydrated.hydra.api.message;

import org.gethydrated.hydra.api.service.SID;

/**
 * Message Interface.
 * 
 * @author Hanno Sternberg
 *
 */
public interface Message {
    
    /**
     * 
     * @return Type of the messages.
     */
    MessageType getType();
    
    /**
     * 
     * @return Destination of the messages.
     */
    SID getDestination();
    
    /**
     * 
     * @return Source of the messages.
     */
    SID getSource();
}
