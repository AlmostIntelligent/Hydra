package org.gethydrated.hydra.core.config;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 *
 */

public class ConfigValue<T> extends ConfigurationItem {
	
	
	public ConfigValue(String _name) {
		super(_name);
	}
	
	public ConfigValue(String _name, T _value){
		super(_name);
		value = _value;
	}

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
	
	public void set(T _value){
		value = _value;
	}
	
	public Object type(){
		return value.getClass();
	}


	
}
