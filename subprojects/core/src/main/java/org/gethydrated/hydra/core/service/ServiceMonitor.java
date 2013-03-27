package org.gethydrated.hydra.core.service;

import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.USID;
import org.gethydrated.hydra.core.messages.*;
import org.gethydrated.hydra.core.sid.DefaultSIDFactory;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class ServiceMonitor {

    private SID self;

    private DefaultSIDFactory sidFactory;

    private Set<Link> links = new HashSet<>();

    private Set<Monitor> monitors = new HashSet<>();

    private volatile boolean closed = false;

    public ServiceMonitor(SID self, DefaultSIDFactory sidFactory) {
        this.self = self;
        this.sidFactory = sidFactory;
    }

    public void addLink(Link link) {
        if(!closed) {
            if(!links.contains(link)) {
                links.add(link);
                SID ref = sidFactory.fromUSID(link.getUsid());
                ref.tell(new Link(self.getUSID()), self);
            }
        } else {
            SID ref = sidFactory.fromUSID(link.getUsid());
            ref.tell(new ServiceExit(self.getUSID(), "already down"),self);
        }
    }

    public void removeLink(Link link) {
        if(!closed) {
            if(links.remove(link)) {
                SID ref = sidFactory.fromUSID(link.getUsid());
                ref.tell(new Unlink(self.getUSID()), self);
            }
        }
    }

    public void addMonitor(Monitor monitor) {
        if(!closed) {
            if(!monitors.contains(monitor)) {
                monitors.add(monitor);
            }
        } else {
            SID ref = sidFactory.fromUSID(monitor.getUsid());
            ref.tell(new ServiceDown(self.getUSID(), "already down"),self);
        }
    }

    public void removeMonitor(Monitor monitor) {
        if(!closed) {
            monitors.remove(monitor);
        }
    }

    /**
     * Closes the service monitor.
     * <p>
     * Registered monitors are notified by
     * {@link org.gethydrated.hydra.core.messages.ServiceDown} messages.
     * </p><p>
     * Registered linked services are notified by
     * {@link org.gethydrated.hydra.core.messages.ServiceExit} messages.
     * </p>
     */
    public void close(String reason) {
        if(!closed) {
            closed = true;
            ServiceExit exit = new ServiceExit(self.getUSID(), reason);
            ServiceDown down = new ServiceDown(self.getUSID(), reason);
            for(Link l : links) {
                SID ref = sidFactory.fromUSID(l.getUsid());
                ref.tell(exit, self);
            }
            for(Monitor m : monitors) {
                SID ref = sidFactory.fromUSID(m.getUsid());
                ref.tell(down, self);
            }
        }
    }

    public boolean isLinked(USID usid) {
        for (Link l : links) {
            if (l.getUsid().equals(usid)) {
                return true;
            }
        }
        return false;
    }
}
