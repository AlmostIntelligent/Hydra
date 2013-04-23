package org.gethydrated.hydra.config.tree;

import org.gethydrated.hydra.api.configuration.ConfigurationItem;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public abstract class ConfigItemBase implements ConfigurationItem {

    /**
     * 
     */
    private final String name;

    /**
     * 
     * @param itemName
     *            .
     */
    public ConfigItemBase(final String itemName) {
        name = itemName;
    }

    /**
     * 
     * @return Item name.
     */
    @Override
    public final String getName() {
        return name;
    }

    /**
     * 
     * @return Copy of this item.
     */
    @Override
    public abstract ConfigurationItem copy();

    /**
     * 
     * @return True, if the item has a value.
     */
    @Override
    public abstract Boolean hasValue();

    /**
     * 
     * @return True, if the item has children.
     */
    @Override
    public abstract Boolean hasChildren();

    /**
     * @param obj
     *            Object for comparison.
     * @return True, if objects are equal.
     */
    @Override
    public abstract boolean equals(Object obj);

    /**
     * @return HashCode;
     */
    @Override
    public abstract int hashCode();

}
