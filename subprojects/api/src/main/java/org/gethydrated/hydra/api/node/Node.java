package org.gethydrated.hydra.api.node;

/**
 * 
 * @author Christian Kulpa
 *
 */
public interface Node {
    
    /**
     * @return Port of the node.
     */
    int getPort();
    
    /**
     * @return IP of the node.
     */
    int getIP();
    
    /**
     * @return (unique) node name.
     */
    String getName();
    
    /**
     * @return True, if this node is local.
     */
    boolean isLocal();
}
