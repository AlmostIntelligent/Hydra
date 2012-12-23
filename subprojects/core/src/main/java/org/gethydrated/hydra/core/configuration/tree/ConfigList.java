package org.gethydrated.hydra.core.configuration.tree;

import java.util.LinkedList;
import java.util.List;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.api.configuration.ConfigItemTypeException;
import org.gethydrated.hydra.api.configuration.ConfigurationItem;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class ConfigList extends ConfigItemBase {

    /**
     * 
     * @param itemName
     *            .
     */
    public ConfigList(final String itemName) {
        super(itemName);
        children = new LinkedList<ConfigurationItem>();
    }

    /**
     * @var List of child item.
     */
    private List<ConfigurationItem> children;

    /**
     * 
     * @return Child items.
     */
    public final List<ConfigurationItem> getChildren() {
        return children;
    }

    /**
     * 
     * @param name
     *            .
     * @return Configuration Item with the given name.
     * @throws ConfigItemNotFoundException .
     */
    public final ConfigurationItem getChild(final String name)
            throws ConfigItemNotFoundException {

        for (ConfigurationItem c : children) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        throw new ConfigItemNotFoundException(name);
    }

    @Override
    public final Boolean hasValue() {
        return false;
    }

    @Override
    public final Object getValue() throws ConfigItemTypeException {
        throw new ConfigItemTypeException();
    }

    @Override
    public final Boolean hasChildren() {
        return children.size() != 0;
    }

    @Override
    public final ConfigurationItem copy() {
        ConfigList l = new ConfigList(getName());
        for (ConfigurationItem i : children) {
            l.getChildren().add(i.copy());
        }
        return l;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        if (children == null) {
            result = prime * result;
        } else {
            result = prime * result + children.hashCode();
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
        ConfigList other = (ConfigList) obj;
        if (!children.equals(other.getChildren())) {
            return false;
        }
        if (getName() == null) {
            if (other.getName() != null) {
                return false;
            }
        } else if (!getName().equals(other.getName())) {
            return false;
        }
        return true;
    }

}
