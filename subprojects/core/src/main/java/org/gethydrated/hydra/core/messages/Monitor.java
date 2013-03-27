package org.gethydrated.hydra.core.messages;

import org.gethydrated.hydra.api.service.USID;

/**
 *
 */
public class Monitor {
    private final USID usid;

    public Monitor(USID usid) {
        this.usid = usid;
    }

    public USID getUsid() {
        return usid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Monitor monitor = (Monitor) o;

        return !(usid != null ? !usid.equals(monitor.usid) : monitor.usid != null);

    }

    @Override
    public int hashCode() {
        return usid != null ? usid.hashCode() : 0;
    }
}
