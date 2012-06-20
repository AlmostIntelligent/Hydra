package org.gethydrated.hydra.api.node;

public interface Node {
    
    int getPort();
    
    int getIP();
    
    String getName();
    
    boolean isLocal();
}
