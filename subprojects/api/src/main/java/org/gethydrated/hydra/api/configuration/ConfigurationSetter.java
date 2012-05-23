package org.gethydrated.hydra.api.configuration;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public interface ConfigurationSetter {

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
     *            item name.
     * @param value
     *            item value.
     */
    void setBoolean(final String name, final Boolean value);

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
     *            item name.
     * @param value
     *            item value.
     */
    void setFloat(final String name, final Double value);

    /**
     * 
     * @param name
     *            item name.
     * @param value
     *            item value.
     */
    void setString(final String name, final String value);

}
