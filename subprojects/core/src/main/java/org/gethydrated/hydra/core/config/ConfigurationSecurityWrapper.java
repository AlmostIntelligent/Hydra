package org.gethydrated.hydra.core.config;

import java.util.List;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.api.configuration.ConfigurationGetter;
import org.gethydrated.hydra.api.configuration.ConfigurationSetter;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0 TO-DO Create real security implementation
 */
public class ConfigurationSecurityWrapper implements ConfigurationGetter,
        ConfigurationSetter {

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
    public final List<String> list(final String name)
            throws ConfigItemNotFoundException {
        // TODO Auto-generated method stub
        return getCoreConfiguration().list(name);
    }

    @Override
    public final Object get(final String name)
            throws ConfigItemNotFoundException {
        return getCoreConfiguration().get(name);
    }

    @Override
    public final void set(final String name, final Object value) {
        getCoreConfiguration().set(name, value);

    }

}
