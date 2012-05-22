package org.gethydrated.hydra.core.config;

import java.util.LinkedList;
import java.util.List;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class ConfigList extends ConfigurationItem {

    /**
     * 
     * @param itemName
     *            .
     */
    public ConfigList(final String itemName) {
        super(itemName);
        childs = new LinkedList<ConfigurationItem>();
    }

    /**
     * @var List of child item.
     */
    private List<ConfigurationItem> childs;

    /**
     * 
     * @return Child items.
     */
    public final List<ConfigurationItem> getChilds() {
        return childs;
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

        for (ConfigurationItem c : childs) {
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
    public final Boolean hasChildren() {
        return childs.size() != 0;
    }

    @Override
    public final ConfigurationItem copy() {
        ConfigList l = new ConfigList(getName());
        for (ConfigurationItem i : childs) {
            l.getChilds().add(i.copy());
        }
        return l;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        if (childs == null) {
            result = prime * result;
        } else {
            result = prime * result + childs.hashCode();
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
        if (childs == null) {
            if (other.childs != null) {
                return false;
            }
        } else if (!childs.equals(other.childs)) {
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
