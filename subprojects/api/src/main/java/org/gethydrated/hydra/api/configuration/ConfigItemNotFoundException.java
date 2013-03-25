package org.gethydrated.hydra.api.configuration;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class ConfigItemNotFoundException extends Exception {

    /**
     * Name of the item.
     */
    private String itemName;

    /**
     * unique ID.
     */
    private static final long serialVersionUID = 1421698206247465742L;

    /**
     * 
     * @param name
     *            Item name.
     */
    public ConfigItemNotFoundException(final String name) {
        setItemName(name);
    }

    /**
     * 
     * @return Item name.
     */
    public final String getItemName() {
        return itemName;
    }

    /**
     * 
     * @param name
     *            .
     */
    private void setItemName(final String name) {
        this.itemName = name;
    }

}
