package org.gethydrated.hydra.chat.messages;

import org.gethydrated.hydra.api.service.USID;

/**
 *
 */
public class NewClient {
    private USID usid;

    public NewClient(USID usid) {
        this.usid = usid;
    }

    private NewClient() {}

    public USID getUSID() {
        return usid;
    }

    @Override
    public String toString() {
        return "NewClient{" +
                "usid=" + usid +
                '}';
    }
}
