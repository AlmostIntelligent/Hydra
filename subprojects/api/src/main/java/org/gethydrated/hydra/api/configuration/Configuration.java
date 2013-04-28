package org.gethydrated.hydra.api.configuration;

import java.util.List;

/**
 * Configuration Interface
 *
 * A configuration is a mapping from full qualified item names to item values.
 *
 * @author Hanno Sternberg
 * @since 0.1.0
 *
 */
public interface Configuration {

    /**
     * Set the value of an item. This function tries to type the item be the
     * type of the value-object.
     *
     * @param name
     *            full qualified item name.
     * @param value
     *            item value.
     */
    void set(final String name, final Object value);

    /**
     * Get the value of an item.
     *
     * @param name
     *            Full qualified item name.
     * @return item value.
     * @throws ConfigItemNotFoundException
     *             if their is no item with the given name.
     */
    Object get(final String name) throws ConfigItemNotFoundException;

    /**
     * Test if the configuration item exists.
     *
     * @param name
     *            Full qualified item name.
     * @return True, if the item exists in the configuration
     */
    Boolean has(final String name);

    /**
     * Accessor function to the configuration tree.
     *
     * @return Root node of the configuration tree.
     */
    ConfigurationItem getRoot();

    /**
     * Accessor function to the configuration tree.
     *
     * @param root
     *            New root node of the configuration
     */
    void setRoot(ConfigurationItem root);

    /**
     * Set a boolean configuration item.
     *
     * @param name
     *            Full qualified item name.
     * @param value
     *            item value.
     */
    void setBoolean(final String name, final Boolean value);

    /**
     * Get a configuration value as boolean.
     *
     * @param name
     *            Full qualified item name.
     * @return item value.
     * @throws ConfigItemNotFoundException
     *             If the item name does not exists.
     */
    Boolean getBoolean(final String name) throws ConfigItemNotFoundException;

    /**
     * Set an integer configuration value.
     *
     * @param name
     *            Full qualified item name.
     * @param value
     *            item value.
     */
    void setInteger(final String name, final Integer value);

    /**
     * Get an integer configuration value.
     *
     * @param name
     *            Full qualified item name.
     * @return item value.
     * @throws ConfigItemNotFoundException
     *             If the item name does not exists.
     */
    Integer getInteger(final String name) throws ConfigItemNotFoundException;

    /**
     * Set a float configuration value.
     *
     * @param name
     *            Full qualified item name.
     * @param value
     *            item value.
     */
    void setFloat(final String name, final Double value);

    /**
     * Get a float configuration value.
     *
     * @param name
     *            Full qualified item name.
     * @return item value.
     * @throws ConfigItemNotFoundException
     *             If the item name does not exists.
     */
    Double getFloat(final String name) throws ConfigItemNotFoundException;

    /**
     * Set a string configuration value.
     *
     * @param name
     *            Full qualified item name.
     * @param value
     *            item value.
     */
    void setString(final String name, final String value);

    /**
     * Get a string configuration value.
     *
     * @param name
     *            Full qualified item name.
     * @return item value.
     * @throws ConfigItemNotFoundException
     *             If the item name does not exists.
     */
    String getString(final String name) throws ConfigItemNotFoundException;

    /**
     * Creates a list ob child items of a given name.
     *
     * @param name
     *            Full qualified item name.
     * @return List of sub items.
     * @throws ConfigItemNotFoundException .
     */
    List<String> list(final String name) throws ConfigItemNotFoundException;

    /**
     * Creates a list ob child items.
     *
     * @return List of sub items.
     * @throws ConfigItemNotFoundException .
     */
    List<String> list() throws ConfigItemNotFoundException;

    /**
     * Creates a new configuration object. The root of the new configuration is
     * defined by the given name.
     *
     * @param base
     *            Full qualified name of the new root node.
     * @return New Configuration object.
     * @throws ConfigItemNotFoundException
     *             If the item name does not exists.
     * @throws ConfigItemTypeException
     *             If the configurationItem is a value.
     */
    Configuration getSubItems(final String base)
            throws ConfigItemNotFoundException, ConfigItemTypeException;

}
