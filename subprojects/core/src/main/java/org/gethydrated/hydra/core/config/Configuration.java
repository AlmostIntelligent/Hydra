package org.gethydrated.hydra.core.config;


/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 *
 */
public interface Configuration {

	public void load(String filename);
	public void save(String filename);
	
	public void set(String name, Object value);
	public Object get(String name);
	
	public void setBoolean(String name, Boolean value);
	public Boolean getBoolean(String name);
	
	public void setInteger(String name, Integer value);
	public Integer getInteger(String name);
	
	public void setFloat(String name, Double value);
	public Double getFloat(String name);
	
	public void setString(String name, String value);
	public String getString(String name);
	
}
