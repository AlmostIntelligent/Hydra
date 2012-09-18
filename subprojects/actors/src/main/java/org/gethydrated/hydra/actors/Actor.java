package org.gethydrated.hydra.actors;

/**
 * 
 * @author Christian Kulpa
 * 
 */
public interface Actor {

    void receive(Object message) throws Exception;
}
