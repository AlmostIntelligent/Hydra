package org.gethydrated.hydra.api.event;

import org.gethydrated.hydra.api.service.USID;

/**
 * ServiceDown event.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 */
public class ServiceDown implements SystemEvent {
    private USID source;
    private USID target;

    private String reason;

    /**
     * Constructor.
     * @param source source service usid.
     * @param target target service usid.
     * @param reason .
     */
    public ServiceDown(final USID source, final USID target,  final String reason) {
        this.source = source;
        this.target = target;
        this.reason = reason;
    }

    @SuppressWarnings("unused")
    private ServiceDown() {
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

    @Override
    public String toString() {
        return "ServiceDown{" +
                "source=" + source +
                ", target=" + target +
                ", reason='" + reason + '\'' +
                '}';
    }
}
