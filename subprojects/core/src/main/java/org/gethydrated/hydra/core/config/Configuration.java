package org.gethydrated.hydra.core.config;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class Configuration {

    protected ConfigList root;

    protected static String CONFIG_SEPERATOR = ".";

    public Configuration() {
        root = new ConfigList("Configuration");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Configuration other = (Configuration) obj;
        if (root == null) {
            if (other.root != null)
                return false;
        } else if (!root.equals(other.root))
            return false;
        return true;
    }

    public ConfigList getRoot() {
        return root;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void setItem(ConfigList base, String name, Object value,
            Class<?> type) {
        String[] namesplit = name.split("\\" + CONFIG_SEPERATOR);
        if (namesplit.length == 1) {
            /* Got value element, set value */
            try {
                ConfigValue child = (ConfigValue) base.getChild(name);
                child.set(value);
            } catch (ConfigItemNotFoundException e) {
                /* Value doesn't exists, add new element */
                base.getChilds().add(new ConfigValue<Object>(name, value));
            }
        } else if (namesplit.length > 1) {
            /* Merge namesplit to tail */
            String nametail = namesplit[1];
            for (int i = 2; i < namesplit.length; i++)
                nametail.concat(CONFIG_SEPERATOR + namesplit[i]);
            ConfigList l;
            try {
                /* List exists: append */
                l = (ConfigList) base.getChild(namesplit[0]);
            } catch (ConfigItemNotFoundException e) {
                /* List doesn't exists: create */
                l = new ConfigList(namesplit[0]);
                base.childs.add(l);
            }
            setItem(l, nametail, value, type);
        }
    }

    public Configuration copy() {
        Configuration cp = new Configuration();
        return cp;
    }

    public void set(String name, Object value) {
        set(name, value, value.getClass());
    }

    public void set(String name, Object value, Class<?> type) {
        setItem(root, name, value, type);
    }

    private Object getFromItem(ConfigList base, String name)
            throws ConfigItemNotFoundException {
        for (ConfigurationItem i : base.getChilds()) {
            if (i.hasValue() && i.getName().equals(name)) {
                return ((ConfigValue<?>) i).value();
            } else if (i.hasChildren()
                    && name.startsWith(i.getName() + CONFIG_SEPERATOR)) {
                return getFromItem((ConfigList) i,
                        name.replace(i.getName() + CONFIG_SEPERATOR, ""));
            }
        }
        throw new ConfigItemNotFoundException(name);
    }

    public Object get(String name) throws ConfigItemNotFoundException {
        return getFromItem(root, name);
    }

    public void setBoolean(String name, Boolean value) {
        set(name, value, value.getClass());
    }

    public Boolean getBoolean(String name) throws ConfigItemNotFoundException {
        return (Boolean) get(name);
    }

    public void setInteger(String name, Integer value) {
        set(name, value, value.getClass());
    }

    public Integer getInteger(String name) throws ConfigItemNotFoundException {
        return (Integer) get(name);
    }

    public void setFloat(String name, Double value) {
        set(name, value, value.getClass());
    }

    public Double getFloat(String name) throws ConfigItemNotFoundException {
        return (Double) get(name);
    }

    public void setString(String name, String value) {
        set(name, value, value.getClass());
    }

    public String getString(String name) throws ConfigItemNotFoundException {
        return (String) get(name);
    }

}
