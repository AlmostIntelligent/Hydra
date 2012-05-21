package org.gethydrated.hydra.core.config;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public abstract class ConfigurationItem {

    /**
     * 
     */
    private String name;

    /**
     * 
     * @param itemName .
     */
    public ConfigurationItem(final String itemName) {
        name = itemName;
    }

    /**
     * 
     * @return Item name.
     */
    public final String getName() {
        return name;
    }

    /**
     * 
     * @return Copy of this item.
     */
    public abstract ConfigurationItem copy();

    /**
     * 
     * @return True, if the item has a value.
     */
    public abstract Boolean hasValue();

    /**
     * 
     * @return True, if the item has children.
     */
    public abstract Boolean hasChildren();

    /**
     * @param obj Object for comparison.
     * @return True, if objects are equal.
     */
    public abstract boolean equals(Object obj);

    /**
     * @return HashCode;
     */
    public abstract int hashCode();

}
