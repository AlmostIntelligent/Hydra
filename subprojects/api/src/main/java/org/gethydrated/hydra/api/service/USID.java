package org.gethydrated.hydra.api.service;

import org.gethydrated.hydra.api.node.Node;

/**
 * Unique service identifier.
 * @author Christian Kulpa
 * @since 0.2.0
 */
public class USID {

    private Long id;
    
    private Node node;
    
    public USID(Long id, Node n) {
        if(id == null || n == null) {
            throw new IllegalStateException("One argument was null: id: "+id+" node: "+n);
        }
        this.id = id;
        this.node = n;
    }
    
    public Long getID() {
        return id;
    }
    
    public Node getNode() {
        return node;
    }
    
    
}
