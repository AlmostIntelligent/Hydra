package org.gethydrated.hydra.actors;

import java.util.concurrent.Future;

/**
 * 
 * @author Christian Kulpa
 *
 */
public interface Reference {

    String getName();
    
    Address getAddress();

    void tell(Object o);
    
    Future<?> ask(Object o);
}
