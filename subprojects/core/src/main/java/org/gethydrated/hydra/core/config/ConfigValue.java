package org.gethydrated.hydra.core.config;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 * @param <T>
 *            .
 */
public class ConfigValue<T> extends ConfigurationItem {

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
    public final Boolean hasChildren() {
        return false;
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
        return new ConfigValue<T>(getName(), value);
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
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

}
