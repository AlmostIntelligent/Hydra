package org.gethydrated.hydra.core.registry;

import org.gethydrated.hydra.api.service.SID;

/**
 * Register service message.
 */
public class RegisterService {

    private final SID sid;

    private final String name;

    /**
     * Constructor.
     * @param sid Service id.
     * @param name Service name.
     */
    public RegisterService(final SID sid, final String name) {
        this.sid = sid;
        this.name = name;
    }

    /**
     * Returns service id.
     * @return service id.
     */
    public SID getSID() {
        return sid;
    }

    /**
     * Returns service name.
     * @return service name.
     */
    public String getName() {
        return name;
    }

}
