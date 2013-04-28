package org.gethydrated.hydra.api.event;

import org.gethydrated.hydra.api.service.USID;

/**
 * Service exit event.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 */
public class ServiceExit implements SystemEvent {
    private USID source;
    private USID target;

    private String reason;

    /**
     * Constructor.
     * @param source source service usid.
     * @param target target service usid.
     * @param reason .
     */
    public ServiceExit(final USID source, final USID target,  final String reason) {
        this.source = source;
        this.target = target;
        this.reason = reason;
    }

    @SuppressWarnings("unused")
    private ServiceExit() {
    }

    public USID getSource() {
        return source;
    }

    public USID getTarget() {
        return target;
    }

    /**
     * Returns the exit reason.
     * @return exit reason.
     */
    public String getReason() {
        return reason;
    }
}
