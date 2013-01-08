package org.gethydrated.hydra.core.configuration.tree;

import org.gethydrated.hydra.api.configuration.ConfigItemTypeException;
import org.gethydrated.hydra.api.configuration.ConfigTypeMismatchException;
import org.gethydrated.hydra.api.configuration.ConfigurationItem;

import java.util.List;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 * @param <T>
 *            .
 */
public class ConfigValue<T> extends ConfigItemBase {

    /**
     * 
     * @param itemName
     *            .
     */
    public ConfigValue(final String itemName) {
        super(itemName);
    }

    /**
     * 
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
    public final List<ConfigurationItem> getChildren() throws ConfigItemTypeException {
        throw new ConfigItemTypeException();
    }

    /**
     * 
     * @return the value.
     */
    public final T value() {
        return value;
    }

    /**
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
        ConfigValue<T> other = (ConfigValue<T>) obj;
        return (value.equals(other.value()));
    }

}
