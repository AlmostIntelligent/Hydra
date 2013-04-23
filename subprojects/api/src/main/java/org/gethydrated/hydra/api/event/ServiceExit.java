package org.gethydrated.hydra.api.event;

import org.gethydrated.hydra.api.service.USID;
import org.gethydrated.hydra.api.service.USIDAware;

/**
 * Service exit event.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 */
public class ServiceExit implements SystemEvent, USIDAware {
    private USID usid;

    private String reason;

    /**
     * Constructor.
     * @param usid service usid.
     * @param reason .
     */
    public ServiceExit(final USID usid, final String reason) {
        this.usid = usid;
        this.reason = reason;
    }

    @SuppressWarnings("unused")
    private ServiceExit() {
    }

    @Override
    public USID getUSID() {
        return usid;
    }

    /**
     * Returns the exit reason.
     * @return exit reason.
     */
    public String getReason() {
        return reason;
    }
}
