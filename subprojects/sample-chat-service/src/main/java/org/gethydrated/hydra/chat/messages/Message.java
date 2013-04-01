package org.gethydrated.hydra.chat.messages;

import org.gethydrated.hydra.api.service.USID;

/**
 *
 */
public class Message {
    private USID usid;
    private String name;

    public Message(USID usid, String name) {
        this.usid = usid;
        this.name = name;
    }

    private Message() {}

    public USID getUsid() {
        return usid;
    }

    public String getName() {
        return name;
    }
}