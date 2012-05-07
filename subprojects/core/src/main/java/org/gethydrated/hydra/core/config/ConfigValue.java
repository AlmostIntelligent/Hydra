package org.gethydrated.hydra.core.config;

import java.io.PrintStream;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 *
 */

public class ConfigValue<T> extends ConfigurationItem {
	
	
	protected T value;
	
	@Override
	public Boolean hasValue(){
		return true;
	}

	@Override
	public Boolean hasChildren() {
		return false;
	}
	
	public T value() {
		return value;
	}
	
	public Object type(){
		return value.getClass();
	}

	@Override
	public void saveToStream(PrintStream stream, int indent) {
		for (int i = 0; i < indent;i++)
			stream.print("\t");
		stream.println("<"+name+">"+value+"</"+name+">");
		
	}
	
	
}
