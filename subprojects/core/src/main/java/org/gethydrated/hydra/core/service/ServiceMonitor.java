package org.gethydrated.hydra.core.service;

import org.gethydrated.hydra.api.event.*;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.USID;
import org.gethydrated.hydra.core.sid.DefaultSIDFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Service monitoring.
 */
public final class ServiceMonitor {

    private final SID self;

    private final DefaultSIDFactory sidFactory;

    private final Set<USID> links = new HashSet<>();

    private final Set<USID> monitors = new HashSet<>();

    private volatile boolean closed = false;

    /**
     * Constructor.
     * @param self Service self reference.
     * @param sidFactory Service id factory.
     */
    public ServiceMonitor(final SID self, final DefaultSIDFactory sidFactory) {
        this.self = self;
        this.sidFactory = sidFactory;
    }

    /**
     * Adds a linked service.
     * @param link linked service.
     */
    public void addLink(final Link link) {
        if (!closed) {
            if (!links.contains(link)) {
                links.add(link.getUSID());
                final SID ref = sidFactory.fromUSID(link.getUSID());
                ref.tell(new Link(self.getUSID()), self);
            }
        } else {
            final SID ref = sidFactory.fromUSID(link.getUSID());
            ref.tell(new ServiceExit(self.getUSID(), "already down"), self);
        }
    }

    /**
     * Removes a linked service.
     * @param link linked service.
     */
    public void removeLink(final Link link) {
        if (!closed) {
            if (links.remove(link)) {
                final SID ref = sidFactory.fromUSID(link.getUSID());
                ref.tell(new Unlink(self.getUSID()), self);
            }
        }
    }

    /**
     * Adds a monitored service.
     * @param monitor monitored service.
     */
    public void addMonitor(final Monitor monitor) {
        if (!closed) {
            if (!monitors.contains(monitor)) {
                monitors.add(monitor.getSource());
            }
        } else {
            final SID ref = sidFactory.fromUSID(monitor.getSource());
            ref.tell(new ServiceDown(self.getUSID(), ref.getUSID(), "already down"), self);
        }
    }

    /**
     * Removes a monitored service.
     * @param monitor monitored service.
     */
    public void removeMonitor(final USID monitor) {
        if (!closed) {
            monitors.remove(monitor);
        }
    }

    /**
     * Closes the service monitor.
     * <p>
     * Registered monitors are notified by
     * {@link org.gethydrated.hydra.api.event.ServiceDown} messages.
     * </p>
     * <p>
     * Registered linked services are notified by
     * {@link org.gethydrated.hydra.api.event.ServiceExit} messages.
     * </p>
     * @param reason Reason for closing.
     */
    public void close(final String reason) {
        if (!closed) {
            closed = true;
            final ServiceExit exit = new ServiceExit(self.getUSID(), reason);
            for (final USID l : links) {
                final SID ref = sidFactory.fromUSID(l);
                ref.tell(exit, self);
            }
            for (final USID m : monitors) {
                final SID ref = sidFactory.fromUSID(m);
                ref.tell(new ServiceDown(self.getUSID(), ref.getUSID(), reason), self);
            }
        }
    }

    /**
     * Returns if the given usid is a linked service.
     * @param usid service usid.
     * @return true if linked.
     */
    public boolean isLinked(final USID usid) {
        for (final USID l : links) {
            if (l.equals(usid)) {
                return true;
            }
        }
        return false;
    }
}
