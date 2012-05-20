package org.gethydrated.hydra.api.service;

import java.io.InputStream;
import java.io.PrintStream;

import org.gethydrated.hydra.api.configuration.ConfigurationGetter;
import org.gethydrated.hydra.api.configuration.ConfigurationSetter;

public interface ServiceContext extends ServiceApi {

    Service getService();
    
    PrintStream getOutputStream();
    
    InputStream getInputStream();
    
    ConfigurationGetter getConfigurationGetter();
    
    ConfigurationSetter getConfigurationSetter();
}
