package org.gethydrated.hydra.actors.node;

import org.gethydrated.hydra.actors.ActorURI;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorSystem;

/**
 * @author Christian Kulpa
 *
 */
public interface ActorNode {
    
    ActorNode getParent();
    
    ActorURI getPath();
    
    String getName();
    
    ActorRef getReference();
    
    ActorSystem getSystem();
}
