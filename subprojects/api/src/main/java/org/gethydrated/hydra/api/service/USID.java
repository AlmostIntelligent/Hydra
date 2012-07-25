package org.gethydrated.hydra.api.service;

import org.gethydrated.hydra.api.node.Node;

/**
 * Unique service identifier.
 * @author Christian Kulpa
 * @since 0.2.0
 */
public class USID {

    /**
     * ID.
     */
    private Long id;
    
    /**
     * Executing node.
     */
    private Node node;
    
    /**
     * 
     * @param id Service ID.
     * @param n executing node.
     */
    public USID(final Long id, final Node n) {
        if (id == null || n == null) {
            throw new IllegalStateException("One argument was null: id: " + id + " node: " + n);
        }
        this.id = id;
        this.node = n;
    }
    
    /**
     * 
     * @return Service ID.
     */
    public final Long getID() {
        return id;
    }
    
    /**
     * 
     * @return Executing node.
     */
    public final Node getNode() {
        return node;
    }
    
    
}
