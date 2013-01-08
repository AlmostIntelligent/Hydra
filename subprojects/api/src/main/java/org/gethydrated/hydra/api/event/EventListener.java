package org.gethydrated.hydra.api.event;

/**
 * Event listener interface for event stream.
 */
public interface EventListener {

    /**
     * Handler for the registered event.
     *
     * @param event Event object.
     */
    void handle(Object event);
}
