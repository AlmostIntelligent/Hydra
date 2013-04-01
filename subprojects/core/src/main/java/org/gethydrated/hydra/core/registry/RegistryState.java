package org.gethydrated.hydra.core.registry;

import org.gethydrated.hydra.api.event.SystemEvent;
import org.gethydrated.hydra.api.service.USID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RegistryState implements SystemEvent {

    private Map<String, USID> registry;

    public RegistryState(Map<String, USID> registry) {
        this.registry = new HashMap<>(registry);
    }

    private RegistryState() {}

    public Map<String, USID> getRegistry() {
        return registry;
    }

    @Override
    public String toString() {
        return "RegistryState{" +
                "registry=" + registry +
                '}';
    }
}
