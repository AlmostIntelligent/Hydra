package org.gethydrated.hydra.core.api;

import java.io.InputStream;
import java.io.PrintStream;

import org.gethydrated.hydra.api.configuration.ConfigurationGetter;
import org.gethydrated.hydra.api.configuration.ConfigurationSetter;
import org.gethydrated.hydra.api.service.Service;
import org.gethydrated.hydra.api.service.ServiceContext;
import org.gethydrated.hydra.core.service.ServiceManager;

/**
 * Service context api implementation.
 * @author Christian Kulpa
 * @since 0.1.0
 *
 */
public class ServiceContextImpl extends ServiceApiImpl implements
        ServiceContext {

    /**
     * Constructor.
     * @param sm ServiceManager.
     */
    public ServiceContextImpl(final ServiceManager sm) {
        super(sm);
    }

    @Override
    public final Service getService() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public final PrintStream getOutputStream() {
        // TODO Auto-generated method stub
        return System.out;
    }

    @Override
    public final InputStream getInputStream() {
        // TODO Auto-generated method stub
        return System.in;
    }

    @Override
    public final ConfigurationGetter getConfigurationGetter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public final ConfigurationSetter getConfigurationSetter() {
        // TODO Auto-generated method stub
        return null;
    }

}
