package org.gethydrated.hydra.core.config;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class Configuration {

    /**
     * 
     */
    private ConfigList root;

    /**
     * 
     */
    private static String configSeparator = ".";

    /**
     * 
     */
    public Configuration() {
        root = new ConfigList("Configuration");
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Configuration other = (Configuration) obj;
        if (root == null) {
            if (other.root != null) {
                return false;
            }
        } else if (!root.equals(other.root)) {
            return false;
        }
        return true;
    }

    /**
     * 
     * @return root item.
     */
    public final ConfigList getRoot() {
        return root;
    }

    /**
     * 
     * @return configuration separation character.
     */
    public static String getConfigSeparator() {
        return configSeparator;
    }

    /**
     * 
     * @param base base item.
     * @param name item name.
     * @param value item value.
     * @param type Type of the item.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public final void setItem(final ConfigList base, final String name, final Object value,
            final Class<?> type) {
        String[] namesplit = name.split("\\" + configSeparator);
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
            for (int i = 2; i < namesplit.length; i++) {
                nametail.concat(configSeparator + namesplit[i]);
            }
            ConfigList l;
            try {
                /* List exists: append */
                l = (ConfigList) base.getChild(namesplit[0]);
            } catch (ConfigItemNotFoundException e) {
                /* List doesn't exists: create */
                l = new ConfigList(namesplit[0]);
                base.getChilds().add(l);
            }
            setItem(l, nametail, value, type);
        }
    }

    /**
     * 
     * @return copy of this configuration.
     */
    public final Configuration copy() {
        Configuration cp = new Configuration();
        return cp;
    }

    /**
     * 
     * @param name .
     * @param value .
     */
    public final void set(final String name, final Object value) {
        set(name, value, value.getClass());
    }

    /**
     * 
     * @param name configuration name.
     * @param value configuration value.
     * @param type configuration value type.
     */
    public final void set(final String name, final Object value, final Class<?> type) {
        setItem(root, name, value, type);
    }

    /**
     * 
     * @param base .
     * @param name configuration name.
     * @return Configuration value.
     * @throws ConfigItemNotFoundException .
     */
    private Object getFromItem(final ConfigList base, final String name)
            throws ConfigItemNotFoundException {
        for (ConfigurationItem i : base.getChilds()) {
            if (i.hasValue() && i.getName().equals(name)) {
                return ((ConfigValue<?>) i).value();
            } else if (i.hasChildren()
                    && name.startsWith(i.getName() + configSeparator)) {
                return getFromItem((ConfigList) i,
                        name.replace(i.getName() + configSeparator, ""));
            }
        }
        throw new ConfigItemNotFoundException(name);
    }

    /**
     * 
     * @param name Item name.
     * @return item value.
     * @throws ConfigItemNotFoundException .
     */
    public final Object get(final String name) throws ConfigItemNotFoundException {
        return getFromItem(root, name);
    }

    /**
     * 
     * @param name item name.
     * @param value item value.
     */
    public final void setBoolean(final String name, final Boolean value) {
        set(name, value, value.getClass());
    }

    /**
     * 
     * @param name Item name.
     * @return item value.
     * @throws ConfigItemNotFoundException .
     */
    public final Boolean getBoolean(final String name) throws ConfigItemNotFoundException {
        return (Boolean) get(name);
    }

    /**
     * 
     * @param name item name.
     * @param value item value.
     */
    public final void setInteger(final String name, final Integer value) {
        set(name, value, value.getClass());
    }

    /**
     * 
     * @param name Item name.
     * @return item value.
     * @throws ConfigItemNotFoundException .
     */
    public final Integer getInteger(final String name) throws ConfigItemNotFoundException {
        return (Integer) get(name);
    }

    /**
     * 
     * @param name item name.
     * @param value item value.
     */
    public final void setFloat(final String name, final Double value) {
        set(name, value, value.getClass());
    }

    /**
     * 
     * @param name Item name.
     * @return item value.
     * @throws ConfigItemNotFoundException .
     */
    public final Double getFloat(final String name) throws ConfigItemNotFoundException {
        return (Double) get(name);
    }

    /**
     * 
     * @param name item name.
     * @param value item value.
     */
    public final void setString(final String name, final String value) {
        set(name, value, value.getClass());
    }

    /**
     * 
     * @param name Item name.
     * @return item value.
     * @throws ConfigItemNotFoundException .
     */
    public final String getString(final String name) throws ConfigItemNotFoundException {
        return (String) get(name);
    }

}
