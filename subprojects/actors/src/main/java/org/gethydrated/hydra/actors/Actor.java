package org.gethydrated.hydra.actors;

import org.gethydrated.hydra.actors.logging.LoggingAdapter;
import org.gethydrated.hydra.actors.node.ActorContext;
import org.gethydrated.hydra.actors.node.ActorNode;
import org.slf4j.Logger;

/**
 * Actor interface.
 * @author Christian Kulpa
 * 
 */
public abstract class Actor {

	private ActorNode node;
	
	public Actor() {
		ActorNode n = ActorNode.getLocalActorNode();
		if(n==null) {
			throw new IllegalStateException();
		}
		this.node = n;
	}
	
    /**
     * Message callback. Invoked on message received.
     * @param message Message object.#
     * @param ctx Actor context.
     * @throws Exception on failure.
     */
    public abstract void onReceive(Object message) throws Exception;
    
    /**
     * Startup callback. Gets called before message processing starts.
     * @param ctx Actor context.
     * @throws Exception on failure.
     */
    public void onStart() throws Exception {}
    
    /**
     * Shutdown callback. Gets called after message processing stops.
     * @param ctx Actor context.
     * @throws Exception on failure.
     */
    public void onStop() throws Exception {}
    
    public ActorContext getContext() {
    	return node.getContext();
    }
    
    public ActorSystem getSystem() {
    	return node.getSystem();
    }
    
    public ActorRef getSelf() {
    	return node.getRef();
    }
    
    public Logger getLogger(Class<?> clazz) {
    	return getLogger(clazz.getName());
    }
    
    public Logger getLogger(String name) {
    	return new LoggingAdapter(name, getSystem());
    }
}
