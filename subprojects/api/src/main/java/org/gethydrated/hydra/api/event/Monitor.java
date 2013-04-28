package org.gethydrated.hydra.api.event;

import org.gethydrated.hydra.api.service.USID;

/**
 * Monitor event.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 */
public class Monitor implements SystemEvent {
    private USID source;
    private USID target;

    /**
     * Constructor.
     * @param target target service usid.
     * @param source source service usid.
     */
    public Monitor(final USID source, final USID target) {
        this.source = source;
        this.target = target;
    }

    @SuppressWarnings("unused")
    private Monitor() {
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

        Monitor monitor = (Monitor) o;

        if (source != null ? !source.equals(monitor.source) : monitor.source != null) return false;
        if (target != null ? !target.equals(monitor.target) : monitor.target != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (target != null ? target.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Monitor{" +
                "source=" + source +
                ", target=" + target +
                '}';
    }
}
