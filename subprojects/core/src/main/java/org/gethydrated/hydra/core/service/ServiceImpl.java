package org.gethydrated.hydra.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.api.event.*;
import org.gethydrated.hydra.api.service.*;
import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.api.ServiceContextImpl;
import org.gethydrated.hydra.core.sid.DefaultSIDFactory;
import org.gethydrated.hydra.core.transport.SerializedObject;
import org.slf4j.Logger;

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

    private final ObjectMapper mapper = new ObjectMapper();

    private final Logger logger;
    /**
     * Constructor.
     *
     * @throws ServiceException on failure.
     */
    public ServiceImpl(String activator, ClassLoader cl, InternalHydra hydra) throws ServiceException {
        this.logger = getLogger(ServiceImpl.class);
        this.cl = cl;
        this.sidFactory = (DefaultSIDFactory) hydra.getDefaultSIDFactory();
        ctx = new ServiceContextImpl(this, hydra);
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
        try {
            activator.stop(ctx);
        } finally {
            monitor.close("stopped");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onReceive(Object message) throws Exception {
        SID sender = null;
        if(getSender() != null) {
            sender = sidFactory.fromActorRef(getSender());
        }
        try {
            message = processMonitor(message);
            if(message instanceof SerializedObject) {
                SerializedObject so = (SerializedObject)message;
                message = mapper.readValue(so.getData(), cl.loadClass(so.getClassName()));
                sender = sidFactory.fromUSID(so.getSender());
            }
        } catch (Exception e) {
            logger.warn("Could not deserialize message: {}", e.getMessage(), e);
            message = null;
        }
        if (message != null) {
            for (Class<?> c : handlers.keySet()) {
                if(c.isInstance(message)) {
                    //noinspection unchecked
                    handlers.get(c).handle(c.cast(message), sender);
                }
            }
        }

    }

    private Object processMonitor(Object message) {
        if(message instanceof Link) {
            monitor.addLink((Link) message);
            return null;
        } else if (message instanceof Unlink) {
            monitor.removeLink(new Link(((Unlink)message).getUSID()));
            return null;
        } else if (message instanceof Monitor) {
            monitor.addMonitor((Monitor) message);
            return null;
        } else if (message instanceof UnMonitor) {
            monitor.removeMonitor(new Monitor(((UnMonitor) message).getUSID()));
            return null;
        } else if (message instanceof ServiceExit) {
            if(monitor.isLinked(((ServiceExit) message).getUSID())) {
                monitor.close(((ServiceExit) message).getReason());
                getContext().stopActor(getSelf());
            }
        }
        return message;
    }

    @Override
    public <T> void  addMessageHandler(Class<T> classifier, MessageHandler<T> messageHandler) {
        handlers.put(classifier, messageHandler);
    }
}
