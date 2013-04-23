package org.gethydrated.hydra.chat.messages;

import org.gethydrated.hydra.api.service.USID;

/**
 * NewClient message.
 */
public class NewClient {
    private USID usid;

    /**
     * Constructor.
     * @param usid Source usid.
     */
    public NewClient(final USID usid) {
        this.usid = usid;
    }

    @SuppressWarnings("unused")
    private NewClient() {
    }

    /**
     * Returns the source usid.
     * @return source usid.
     */
    public USID getUSID() {
        return usid;
    }

    @Override
    public String toString() {
        return "NewClient{" + "usid=" + usid + '}';
    }
}
