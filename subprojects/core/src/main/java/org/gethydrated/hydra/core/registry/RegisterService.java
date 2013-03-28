package org.gethydrated.hydra.core.registry;

import org.gethydrated.hydra.api.service.SID;

/**
 *
 */
public class RegisterService {

    private final SID sid;

    private final String name;

    public RegisterService(SID sid, String name) {
        this.sid = sid;
        this.name = name;
    }

    public SID getSID() {
        return sid;
    }

    public String getName() {
        return name;
    }

}
