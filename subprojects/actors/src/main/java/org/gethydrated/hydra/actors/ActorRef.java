package org.gethydrated.hydra.actors;

import java.util.concurrent.Future;

/**
 * 
 * @author Christian Kulpa
 *
 */
public interface ActorRef {

    String getName();
    
    ActorURI getAddress();

	void tell(Object o, ActorRef sender);
    
    void forward(Object o, ActorRef ref);
    
    Future<?> ask(Object o);


}
