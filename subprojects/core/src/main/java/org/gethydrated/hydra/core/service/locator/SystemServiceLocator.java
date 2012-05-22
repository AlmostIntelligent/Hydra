package org.gethydrated.hydra.core.service.locator;

import java.net.URL;

import org.gethydrated.hydra.core.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * System service locator.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 * 
 */
public class SystemServiceLocator implements ServiceLocator {
    
    private static final Logger LOG = LoggerFactory.getLogger(SystemServiceLocator.class);
    
    private final String systemServiceDir;
    
    public SystemServiceLocator(Configuration cfg) {
        systemServiceDir = System.getProperty("hydra.home") + "/service/system";
        LOG.info(systemServiceDir);
    }

    @Override
    public final URL locate(final String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public final URL locate(final String name, final String version) {
        // TODO Auto-generated method stub
        return null;
    }

}
