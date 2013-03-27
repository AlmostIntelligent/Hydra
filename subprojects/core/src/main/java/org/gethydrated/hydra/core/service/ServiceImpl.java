package org.gethydrated.hydra.core.service;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.api.service.*;
import org.gethydrated.hydra.core.api.ServiceContextImpl;
import org.gethydrated.hydra.core.messages.*;
import org.gethydrated.hydra.core.sid.DefaultSIDFactory;

import java.util.HashMap;
import java.util.Map;
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

    private final Map<String, Boolean> serviceFlags = new HashMap<>();

    private final ServiceMonitor monitor;
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
        monitor = new ServiceMonitor(sidFactory.fromActorRef(getSelf()), sidFactory);
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

    @Override
    public void onStop() throws Exception {
        activator.stop(ctx);
        monitor.close("stopped");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onReceive(Object message) throws Exception {
        SID sender = null;
        if(getSender() != null) {
            sender = sidFactory.fromActorRef(getSender());
        }
        try {
            processMonitor(message);
        } catch (Throwable t) {
                t.printStackTrace();
            }
        for (Class<?> c : handlers.keySet()) {
            if(c.isInstance(message)) {
                //noinspection unchecked
                handlers.get(c).handle(c.cast(message), sender);
            }
        }
    }

    private void processMonitor(Object message) {
        if(message instanceof Link) {
            monitor.addLink((Link) message);
        } else if (message instanceof Unlink) {
            monitor.removeLink(new Link(((Unlink)message).getUsid()));
        } else if (message instanceof Monitor) {
            monitor.addMonitor((Monitor) message);
        } else if (message instanceof UnMonitor) {
            monitor.removeMonitor(new Monitor(((UnMonitor) message).getUsid()));
        } else if (message instanceof ServiceExit) {
            if(monitor.isLinked(((ServiceExit) message).getUsid())) {
                monitor.close(((ServiceExit) message).getReason());
                getContext().stop(getSelf());
            }
        }
    }

    @Override
    public <T> void  addMessageHandler(Class<T> classifier, MessageHandler<T> messageHandler) {
        handlers.put(classifier, messageHandler);
    }
}
