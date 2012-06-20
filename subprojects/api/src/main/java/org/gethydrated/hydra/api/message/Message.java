package org.gethydrated.hydra.api.message;

import org.gethydrated.hydra.api.service.USID;

public interface Message {
    
    MessageType getType();
    
    USID getTarget();
}
