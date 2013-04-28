package org.gethydrated.hydra.api.service;

import java.util.concurrent.Future;

/**
 * Unique service identifier.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 */
public interface SID {
    
    /**
     * Returns the service usid.
     * @return service usid.
     */
    USID getUSID();

    /**
     * Sends a message to the service id.
     * @param message message object.
     * @param sender message sender.
     */
    void tell(Object message, SID sender);

    /**
     * Asks a message.
     * @param message message object.
     * @return answer message object.
     */
    Future<?> ask(Object message);

}
