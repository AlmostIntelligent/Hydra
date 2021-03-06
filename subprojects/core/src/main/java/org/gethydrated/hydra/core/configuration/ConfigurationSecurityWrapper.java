package org.gethydrated.hydra.core.configuration;

import java.util.List;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.api.configuration.ConfigItemTypeException;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.api.configuration.ConfigurationItem;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0 TO-DO Create real security implementation
 */
public class ConfigurationSecurityWrapper implements Configuration {

    /**
     * @var Reference to the core configuration.
     */
    private Configuration coreConfiguration;

    /**
     * 
     * @return core Configuration
     */
    private Configuration getCoreConfiguration() {
        return coreConfiguration;
    }

    /**
     * 
     * @param coreConfig
     *            Core Configuration.
     */
    private void setCoreConfiguration(final Configuration coreConfig) {
        this.coreConfiguration = coreConfig;
    }

    /**
     * 
     * @param coreConfig
     *            core Configuration
     */
    public ConfigurationSecurityWrapper(final Configuration coreConfig) {
        setCoreConfiguration(coreConfig);
    }

    @Override
    public final void setBoolean(final String name, final Boolean value) {
        // TODO Auto-generated method stub
        getCoreConfiguration().setBoolean(name, value);
    }

    @Override
    public final void setInteger(final String name, final Integer value) {
        // TODO Auto-generated method stub
        getCoreConfiguration().setInteger(name, value);
    }

    @Override
    public final void setFloat(final String name, final Double value) {
        // TODO Auto-generated method stub
        getCoreConfiguration().setFloat(name, value);
    }

    @Override
    public final void setString(final String name, final String value) {
        // TODO Auto-generated method stub
        getCoreConfiguration().setString(name, value);
    }

    @Override
    public final Boolean getBoolean(final String name)
            throws ConfigItemNotFoundException {
        // TODO Auto-generated method stub
        return getCoreConfiguration().getBoolean(name);
    }

    @Override
    public final Integer getInteger(final String name)
            throws ConfigItemNotFoundException {
        // TODO Auto-generated method stub
        return getCoreConfiguration().getInteger(name);
    }

    @Override
    public final Double getFloat(final String name)
            throws ConfigItemNotFoundException {
        // TODO Auto-generated method stub
        return getCoreConfiguration().getFloat(name);
    }

    @Override
    public final String getString(final String name)
            throws ConfigItemNotFoundException {
        // TODO Auto-generated method stub
        return getCoreConfiguration().getString(name);
    }

    @Override
    public final List<String> list() throws ConfigItemNotFoundException {
        return getCoreConfiguration().list();
    }

    @Override
    public final List<String> list(final String name)
            throws ConfigItemNotFoundException {
        // TODO Auto-generated method stub
        return getCoreConfiguration().list(name);
    }

    @Override
    public Configuration getSubItems(final String base)
            throws ConfigItemNotFoundException, ConfigItemTypeException {
        return getCoreConfiguration().getSubItems(base);
    }

    @Override
    public final Object get(final String name)
            throws ConfigItemNotFoundException {
        return getCoreConfiguration().get(name);
    }

    @Override
    public Boolean has(final String name) {
        return getCoreConfiguration().has(name);
    }

    @Override
    public ConfigurationItem getRoot() {
        return getCoreConfiguration().getRoot();
    }

    @Override
    public void setRoot(final ConfigurationItem root) {
        getCoreConfiguration().setRoot(root);
    }

    @Override
    public final void set(final String name, final Object value) {
        getCoreConfiguration().set(name, value);

    }

}
