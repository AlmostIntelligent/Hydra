package org.gethydrated.hydra.core.service;

import java.net.URLClassLoader;

import org.gethydrated.hydra.api.service.Service;
import org.gethydrated.hydra.api.service.ServiceActivator;
import org.gethydrated.hydra.api.service.ServiceException;
import org.gethydrated.hydra.core.api.ServiceContextImpl;

/**
 * Service implementation.
 * @author Christian Kulpa
 * @since 0.1.0
 *
 */
public class ServiceImpl implements Service {

    /**
     * Service activator.
     */
    private final ServiceActivator activator;
    
    private final ServiceInfo serviceInfo;
    
    private final ClassLoader cl;

    
    public ServiceImpl(ServiceInfo si) throws ServiceException {
        serviceInfo = si;
        cl = new URLClassLoader(si.getServiceJars(), ServiceImpl.class.getClassLoader());
        try {
            Class clzz = cl.loadClass(si.getActivator());
            if(clzz == null) {
                throw new ServiceException("Service activator not found:" +si.getActivator());
            }
            activator = (ServiceActivator) clzz.newInstance();
        } catch (Exception e) {
            throw new ServiceException(e);
        } 
    }

    @Override
    public final void start() throws ServiceException {
        try {
            activator.start(new ServiceContextImpl());
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void stop() throws ServiceException {

    }

    @Override
    public Long getId() {
        // TODO Auto-generated method stub
        return (long) 0;
    }

}
