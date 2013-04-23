package org.gethydrated.hydra.api.event;

/**
 * Event listener interface for event stream.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 */
public interface EventListener {

    /**
     * Handler for the registered event.
     * 
     * @param event
     *            Event object.
     */
    void handle(Object event);
}
