package org.gethydrated.hydra.core.config;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class ConfigList extends ConfigurationItem {

    public ConfigList(String _name) {
        super(_name);
        childs = new LinkedList<ConfigurationItem>();
    }

    protected String name;
    protected List<ConfigurationItem> childs;

    public List<ConfigurationItem> getChilds() {
        return childs;
    }

    public ConfigurationItem getChild(String name)
            throws ConfigItemNotFoundException {

        for (ConfigurationItem c : childs)
            if (c.getName().equals(name))
                return c;
        throw new ConfigItemNotFoundException(name);
    }

    @Override
    public Boolean hasValue() {
        return false;
    }

    @Override
    public Boolean hasChildren() {
        return childs.size() != 0;
    }

    @Override
    public ConfigurationItem copy() {
        ConfigList l = new ConfigList(name);
        for (ConfigurationItem i : childs)
            l.getChilds().add(i.copy());
        return l;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        ConfigList other = (ConfigList) obj;
        if (childs == null) {
            if (other.childs != null)
                return false;
        } else if (!childs.equals(other.childs))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
