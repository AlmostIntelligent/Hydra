package org.gethydrated.hydra.api.event;

import org.gethydrated.hydra.api.service.USID;
import org.gethydrated.hydra.api.service.USIDAware;

/**
 * Link event. Links two services together.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 */
public class Link implements SystemEvent, USIDAware {
    private USID usid;

    /**
     * Constructor.
     * @param usid service usid.
     */
    public Link(final USID usid) {
        this.usid = usid;
    }

    @SuppressWarnings("unused")
    private Link() {
    }

    @Override
    public USID getUSID() {
        return usid;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Link link = (Link) o;

        return !(usid != null ? !usid.equals(link.usid) : link.usid != null);

    }

    @Override
    public int hashCode() {
        return usid != null ? usid.hashCode() : 0;
    }
}
