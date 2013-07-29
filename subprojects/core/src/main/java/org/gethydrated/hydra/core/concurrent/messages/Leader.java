package org.gethydrated.hydra.core.concurrent.messages;

import org.gethydrated.hydra.api.event.SystemEvent;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.UUID;

/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Leader implements SystemEvent {

    private UUID node;

    private Leader() {
    }

    public Leader(UUID node) {
        this.node = node;
    }

    public UUID getNode() {
        return node;
    }

}
