package org.gethydrated.hydra.api.configuration;

import java.util.List;

/**
 * Hydra configuration.
 * 
 * @author Hanno Sternberg
 * @author Christian Kulpa
 * @since 0.1.0 
 */
public interface Configuration {

    /**
     * 
     * @param name
     *            item name.
     * @param value
     *            item value.
     */
    void set(final String name, final Object value);

    /**
     * 
     * @param name
     *            Item name.
     * @return item value.
     * @throws ConfigItemNotFoundException .
     */
    Object get(final String name) throws ConfigItemNotFoundException;

    /**
     * 
     * @param name .
     * @return .
     */
    Boolean has(final String name);

    /**
     * 
     * @return .
     */
    ConfigurationItem getRoot();

    /**
     * 
     * @param root .
     */
    void setRoot(ConfigurationItem root);

    /**
     * 
     * @param name
     *            item name.
     * @param value
     *            item value.
     */
    void setBoolean(final String name, final Boolean value);

    /**
     * 
     * @param name
     *            Item name.
     * @return item value.
     * @throws ConfigItemNotFoundException .
     */
    Boolean getBoolean(final String name) throws ConfigItemNotFoundException;

    /**
     * 
     * @param name
     *            item name.
     * @param value
     *            item value.
     */
    void setInteger(final String name, final Integer value);

    /**
     * 
     * @param name
     *            Item name.
     * @return item value.
     * @throws ConfigItemNotFoundException .
     */
    Integer getInteger(final String name) throws ConfigItemNotFoundException;

    /**
     * 
     * @param name
     *            item name.
     * @param value
     *            item value.
     */
    void setFloat(final String name, final Double value);

    /**
     * 
     * @param name
     *            Item name.
     * @return item value.
     * @throws ConfigItemNotFoundException .
     */
    Double getFloat(final String name) throws ConfigItemNotFoundException;

    /**
     * 
     * @param name
     *            item name.
     * @param value
     *            item value.
     */
    void setString(final String name, final String value);

    /**
     * 
     * @param name
     *            Item name.
     * @return item value.
     * @throws ConfigItemNotFoundException .
     */
    String getString(final String name) throws ConfigItemNotFoundException;

    /**
     * 
     * @param name
     *            Item name.
     * @return List of sub items.
     * @throws ConfigItemNotFoundException .
     */
    List<String> list(final String name) throws ConfigItemNotFoundException;

    /**
     * 
     * @return .
     * @throws ConfigItemNotFoundException .
     */
    List<String> list() throws ConfigItemNotFoundException;

    /**
     * 
     * @param base .
     * @return .
     * @throws ConfigItemNotFoundException .
     * @throws ConfigItemTypeException .
     */
    Configuration getSubItems(final String base)
            throws ConfigItemNotFoundException, ConfigItemTypeException;

}
