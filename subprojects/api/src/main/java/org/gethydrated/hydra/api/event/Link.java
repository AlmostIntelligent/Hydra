package org.gethydrated.hydra.api.event;

import org.gethydrated.hydra.api.service.USID;

/**
 * Link event. Links two services together.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 */
public class Link implements SystemEvent {
    private USID source;
    private USID target;

    /**
     * Constructor.
     * @param target target service usid.
     * @param source source service usid.
     */
    public Link(final USID source, final USID target) {
        this.source = source;
        this.target = target;
    }

    @SuppressWarnings("unused")
    private Link() {
    }

    public USID getSource() {
        return source;
    }

    public USID getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Link link = (Link) o;

        if (source != null ? !source.equals(link.source) : link.source != null) return false;
        if (target != null ? !target.equals(link.target) : link.target != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (target != null ? target.hashCode() : 0);
        return result;
    }
}
