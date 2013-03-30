package org.gethydrated.hydra.chat.messages;

import org.gethydrated.hydra.api.service.USID;

/**
 *
 */
public class Renamed {
    private USID usid;
    private String name;

    public Renamed(USID usid, String name) {
        this.usid = usid;
        this.name = name;
    }

    private Renamed() {}

    public USID getUsid() {
        return usid;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Renamed{" +
                "usid=" + usid +
                ", name='" + name + '\'' +
                '}';
    }
}
