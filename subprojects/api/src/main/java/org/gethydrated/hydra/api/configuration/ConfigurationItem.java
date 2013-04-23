package org.gethydrated.hydra.api.configuration;

import java.util.List;

/**
 * ConfigurationItem interface.
 *
 * A configuration item is either a specific value or holds a list of sub items.
 * This results into the configuration being a tree-like structure.
 *
 * @author Hanno Sternberg
 */
public interface ConfigurationItem {

    /**
     *
     * @return The name of the item.
     */
    String getName();

    /**
     * Test for children of the item.
     *
     * @return True, if item has child items.
     */
    Boolean hasChildren();

    /**
     *
     * @return A list of child items.
     * @throws ConfigItemTypeException
     *             if the configurationItem is a value item.
     */
    List<ConfigurationItem> getChildren() throws ConfigItemTypeException;

    /**
     * Test for the item to be a value.
     *
     * @return True, if the item is a value.
     */
    Boolean hasValue();

    /**
     *
     * @return The value of the item.
     * @throws ConfigItemTypeException
     *             if the configurationItem is a list item.
     */
    Object getValue() throws ConfigItemTypeException;

    /**
     * Test if two configuration items are the same.
     *
     * @param other
     *            The object to test.
     * @return True, if the object are equal.
     */
    @Override
    boolean equals(final Object other);

    /**
     *
     * @return A deep copy of the ConfigurationItem
     */
    ConfigurationItem copy();
}
