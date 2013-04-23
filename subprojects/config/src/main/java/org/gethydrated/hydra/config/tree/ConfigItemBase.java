package org.gethydrated.hydra.config.tree;

import org.gethydrated.hydra.api.configuration.ConfigurationItem;

/**
 * Base class for Configuration items.
 *
 * @author Hanno Sternberg
 * @since 0.1.0
 *
 */
public abstract class ConfigItemBase implements ConfigurationItem {

    /**
     * Name of the item.
     */
    private final String name;

    /**
     * Constructor.
     *
     * @param itemName
     *            item name.
     */
    public ConfigItemBase(final String itemName) {
        name = itemName;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public abstract ConfigurationItem copy();

    @Override
    public abstract Boolean hasValue();

    @Override
    public abstract Boolean hasChildren();

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

}
