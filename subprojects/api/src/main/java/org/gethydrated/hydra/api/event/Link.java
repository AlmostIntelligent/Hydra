package org.gethydrated.hydra.api.event;

import org.gethydrated.hydra.api.service.USID;

/**
 *
 */
public class Link implements SystemEvent {
    private USID usid;

    public Link(USID usid) {
        this.usid = usid;
    }

    private Link() {}

    public USID getUsid() {
        return usid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Link link = (Link) o;

        return !(usid != null ? !usid.equals(link.usid) : link.usid != null);

    }

    @Override
    public int hashCode() {
        return usid != null ? usid.hashCode() : 0;
    }
}
