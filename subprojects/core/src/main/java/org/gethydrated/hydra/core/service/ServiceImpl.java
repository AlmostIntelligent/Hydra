package org.gethydrated.hydra.core.service;

import java.net.URLClassLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.api.service.*;
import org.gethydrated.hydra.core.api.ServiceContextImpl;
import org.gethydrated.hydra.api.configuration.Configuration;

/**
 * Service implementation.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 * 
 */
public class ServiceImpl extends Actor implements Service {

    /**
     * Service activator.
     */
    private final ServiceActivator activator;

    /**
     * Service context.
     */
    private final ServiceContext ctx;
    
    /**
     * Service classloader.
     */
    private final ClassLoader cl;

    private final ConcurrentMap<Class<?>, MessageHandler> handlers = new ConcurrentHashMap<>();

    /**
     * Constructor.
     * @param si Service informations.
     * @param cfg Configuration.
     * @throws ServiceException on failure.
     */
    public ServiceImpl(final ServiceInfo si, final Configuration cfg) throws ServiceException {
        cl = new URLClassLoader(si.getServiceJars(),
                ServiceImpl.class.getClassLoader().getParent());
        ctx = new ServiceContextImpl(this, cfg);
        try {
            Class<?> clazz = cl.loadClass(si.getActivator());
            if (clazz == null) {
                throw new ServiceException("Service activator not found:"
                        + si.getActivator());
            }
            activator = (ServiceActivator) clazz.newInstance();
        } catch (Exception | NoClassDefFoundError e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void onStart() throws Exception {
        activator.start(ctx);
    }

    public void onStop() throws Exception {
        activator.stop(ctx);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        SID sender = null;
        if(getSender() != null) {
            sender = new SIDImpl(getSender());
        }
        for (Class<?> c : handlers.keySet()) {
            if(c.isInstance(message)) {
                //noinspection unchecked
                handlers.get(c).handle(c.cast(message), sender);
            }
        }
    }

    @Override
    public <T> void  addMessageHandler(Class<T> classifier, MessageHandler<T> messageHandler) {
        handlers.put(classifier, messageHandler);
    }
}
