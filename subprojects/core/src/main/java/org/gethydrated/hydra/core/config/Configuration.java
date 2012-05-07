package org.gethydrated.hydra.core.config;


/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 *
 */
public class Configuration {

	protected ConfigurationItem root;
	
	public Configuration(){
		root = new ConfigList("Configuration");
	}
	
	public void set(String name, Object value) throws ConfigItemNotFoundException{
		throw new ConfigItemNotFoundException(name);
	}
	
	private Object getFromItem(ConfigurationItem base, String name) throws ConfigItemNotFoundException {
		if (base.hasValue() && base.getName() == name)
			return ((ConfigValue<?>)base).value();
		throw new ConfigItemNotFoundException(name);
	}
	
	public Object get(String name) throws ConfigItemNotFoundException{
		return getFromItem(root, name);
	}
	
	public void setBoolean(String name, Boolean value) throws ConfigItemNotFoundException{ 
		set(name, value);
	}
	
	public Boolean getBoolean(String name) throws ConfigItemNotFoundException{ 
		return (Boolean) get(name); 
	}
	
	public void setInteger(String name, Integer value) throws ConfigItemNotFoundException{
		set(name, value);
	}
	
	public Integer getInteger(String name) throws ConfigItemNotFoundException{ 
		return (Integer) get(name); 
	}
	
	public void setFloat(String name, Double value) throws ConfigItemNotFoundException{
		set(name, value);
	}
	
	public Double getFloat(String name) throws ConfigItemNotFoundException{ 
		return (Double) get(name); 
	}
	
	public void setString(String name, String value) throws ConfigItemNotFoundException{
		set(name, value);
	}
	
	public String getString(String name) throws ConfigItemNotFoundException{
		return (String) get(name);
	}
	
}
