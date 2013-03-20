package org.gethydrated.hydra.core.service;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.api.service.*;
import org.gethydrated.hydra.core.api.ServiceContextImpl;
import org.gethydrated.hydra.core.sid.DefaultSIDFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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

    private final DefaultSIDFactory sidFactory;
    /**
     * Constructor.
     *
     * @param cfg Configuration.
     * @param sidFactory
     * @throws ServiceException on failure.
     */
    public ServiceImpl(String activator, ClassLoader cl, Configuration cfg, DefaultSIDFactory sidFactory) throws ServiceException {
        this.cl = cl;
        this.sidFactory = sidFactory;
        ctx = new ServiceContextImpl(this, cfg, sidFactory);
        try {
            Class<?> clazz = cl.loadClass(activator);
            if (clazz == null) {
                throw new ServiceException("Service activator not found:"
                        + activator);
            }
            this.activator = (ServiceActivator) clazz.newInstance();
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
    @SuppressWarnings("unchecked")
    public void onReceive(Object message) throws Exception {
        SID sender = null;
        if(getSender() != null) {
            sender = sidFactory.fromActorRef(getSender());
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
