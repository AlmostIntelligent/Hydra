package org.gethydrated.hydra.core.config;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public abstract class ConfigurationItem {

    protected String name;

    public ConfigurationItem(String _name) {
        name = _name;
    }

    public String getName() {
        return name;
    }

    public abstract ConfigurationItem copy();

    public abstract Boolean hasValue();

    public abstract Boolean hasChildren();

    public abstract boolean equals(Object obj);

}
