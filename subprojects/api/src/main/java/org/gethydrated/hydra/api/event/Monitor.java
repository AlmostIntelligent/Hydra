package org.gethydrated.hydra.api.event;

import org.gethydrated.hydra.api.service.USID;
import org.gethydrated.hydra.api.service.USIDAware;

/**
 *
 */
public class Monitor implements SystemEvent, USIDAware {
    private USID usid;

    public Monitor(USID usid) {
        this.usid = usid;
    }

    private Monitor() {}

    public USID getUSID() {
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

    @Override
    public String toString() {
        return "Monitor{" +
                "usid=" + usid +
                '}';
    }

}
