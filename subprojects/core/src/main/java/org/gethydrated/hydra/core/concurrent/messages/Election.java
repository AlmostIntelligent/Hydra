package org.gethydrated.hydra.core.concurrent.messages;

import org.gethydrated.hydra.api.event.SystemEvent;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.UUID;

/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Election implements SystemEvent {
    private int id;
    private UUID uuid;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "Election{" +
                "id=" + id +
                ", uuid=" + uuid +
                '}';
    }
}
