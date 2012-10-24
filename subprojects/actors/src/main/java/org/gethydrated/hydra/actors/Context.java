package org.gethydrated.hydra.actors;

import org.slf4j.Logger;

/**
 * 
 * @author Christian Kulpa
 *
 */
public abstract class Context {
	public abstract Logger getLogger();
	
	public abstract System getSystem();
}
