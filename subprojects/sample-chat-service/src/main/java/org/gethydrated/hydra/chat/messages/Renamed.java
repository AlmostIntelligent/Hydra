package org.gethydrated.hydra.chat.messages;

import org.gethydrated.hydra.api.service.USID;

/**
 * Renamed message.
 */
public class Renamed {
    private USID usid;
    private String name;
    
    /**
     * Constructor.
     * @param usid Source USID.
     * @param name Client name.
     */
    public Renamed(final USID usid, final String name) {
        this.usid = usid;
        this.name = name;
    }

    @SuppressWarnings("unused")
    private Renamed() {
    }

    /**
     * Returns the client usid.
     * @return client usid.
     */
    public USID getUsid() {
        return usid;
    }

    /**
     * Returns the clients name.
     * @return client name.
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Renamed{" + "usid=" + usid + ", name='" + name + '\'' + '}';
    }
}
