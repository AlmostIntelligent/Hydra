package org.gethydrated.hydra.core.config;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 *
 */
public class ConfigItemNotFoundException extends Exception {

	private String itemName;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1421698206247465742L;
	
	public ConfigItemNotFoundException(String name){
		setItemName(name);
	}

	public String getItemName() {
		return itemName;
	}

	private void setItemName(String itemName) {
		this.itemName = itemName;
	}

}
