package org.gethydrated.hydra.core.api;

import java.io.InputStream;
import java.io.PrintStream;

import org.gethydrated.hydra.api.configuration.ConfigurationGetter;
import org.gethydrated.hydra.api.configuration.ConfigurationSetter;
import org.gethydrated.hydra.api.service.Service;
import org.gethydrated.hydra.api.service.ServiceContext;

public class ServiceContextImpl extends ServiceApiImpl implements
        ServiceContext {

    @Override
    public Service getService() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PrintStream getOutputStream() {
        // TODO Auto-generated method stub
        return System.out;
    }

    @Override
    public InputStream getInputStream() {
        // TODO Auto-generated method stub
        return System.in;
    }

    @Override
    public ConfigurationGetter getConfigurationGetter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConfigurationSetter getConfigurationSetter() {
        // TODO Auto-generated method stub
        return null;
    }

}
