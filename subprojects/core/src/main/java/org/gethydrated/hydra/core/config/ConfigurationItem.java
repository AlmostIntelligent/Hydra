package org.gethydrated.hydra.core.config;

import java.io.PrintStream;


/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 *
 */
public abstract class ConfigurationItem {

	protected String name;
	
	public ConfigurationItem(String _name){
		name = _name;
	}
	
	public String getName(){
		return name;
	}
	
	public abstract Boolean hasValue();
	public abstract Boolean hasChildren();
	
	public abstract void saveToStream(PrintStream stream, int indent);
	public void saveToStream(PrintStream stream){
		saveToStream(stream,0);
	}
}
