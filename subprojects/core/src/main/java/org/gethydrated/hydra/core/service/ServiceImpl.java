package org.gethydrated.hydra.core.service;

import java.net.URLClassLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.gethydrated.hydra.api.service.Service;
import org.gethydrated.hydra.api.service.ServiceActivator;
import org.gethydrated.hydra.api.service.ServiceException;
import org.gethydrated.hydra.core.api.ServiceContextImpl;

/**
 * Service implementation.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 * 
 */
public class ServiceImpl implements Service {

    /**
     * Service activator.
     */
    private final ServiceActivator activator;

    /**
     * Service classloader.
     */
    private final ClassLoader cl;

    /**
     * Service threadpool.
     */
    private final ExecutorService threadpool = Executors.newCachedThreadPool();
    
    /**
     * Threadpool timeout.
     */
    private static final int TIMEOUT = 5;

    /**
     * Constructor.
     * @param si Service informations.
     * @throws ServiceException on failure.
     */
    public ServiceImpl(final ServiceInfo si) throws ServiceException {
        cl = new URLClassLoader(si.getServiceJars(),
                ServiceImpl.class.getClassLoader().getParent());
        try {
            Class<?> clzz = cl.loadClass(si.getActivator());
            if (clzz == null) {
                throw new ServiceException("Service activator not found:"
                        + si.getActivator());
            }
            activator = (ServiceActivator) clzz.newInstance();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public final void start() throws ServiceException {
        try {
            threadpool.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        activator.start(new ServiceContextImpl(null));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public final void stop() throws ServiceException {
        threadpool.shutdown();
        try {
            activator.stop(null);
            threadpool.awaitTermination(TIMEOUT, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new ServiceException(e);
        }

    }

    @Override
    public final Long getId() {
        // TODO Auto-generated method stub
        return (long) 0;
    }

}
