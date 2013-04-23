package org.gethydrated.hydra.config.tree;

import java.util.List;

import org.gethydrated.hydra.api.configuration.ConfigItemTypeException;
import org.gethydrated.hydra.api.configuration.ConfigurationItem;

/**
 * Configuration Value.
 *
 * Stores a single value. Is always a leaf in the configuration tree.
 *
 * @author Hanno Sternberg
 * @since 0.1.0
 * @param <T>
 *            Type of the item.
 */
public class ConfigValue<T> extends ConfigItemBase {

    /**
     * Constructor.
     *
     * @param itemName
     *            Name of the item.
     */
    public ConfigValue(final String itemName) {
        super(itemName);
    }

    /**
     * @param itemName
     *            .
     * @param itemValue
     *            .
     */
    public ConfigValue(final String itemName, final T itemValue) {
        super(itemName);
        value = itemValue;
    }

    /**
     * @var The value.
     */
    private T value;

    @Override
    public final Boolean hasValue() {
        return true;
    }

    @Override
    public final Object getValue() {
        return value;
    }

    @Override
    public final Boolean hasChildren() {
        return false;
    }

    @Override
    public final List<ConfigurationItem> getChildren()
            throws ConfigItemTypeException {
        throw new ConfigItemTypeException();
    }

    /**
     * Get the item value.
     *
     * @return the value.
     */
    public final T value() {
        return value;
    }

    /**
     * Set the item value.
     *
     * @param itemValue
     *            the value.
     */
    public final void set(final T itemValue) {
        value = itemValue;
    }

    /**
     *
     * @return Type of the value.
     */
    public final Object type() {
        return value.getClass();
    }

    @Override
    public final ConfigurationItem copy() {
        return new ConfigValue<>(getName(), value);
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        if (value == null) {
            result = prime * result;
        } else {
            result = prime * result + value.hashCode();
        }
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("unchecked")
        final
        ConfigValue<T> other = (ConfigValue<T>) obj;
        return (value.equals(other.value()));
    }

}
