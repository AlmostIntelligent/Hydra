package org.gethydrated.hydra.core.registry;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.gethydrated.hydra.api.event.SystemEvent;
import org.gethydrated.hydra.api.service.USID;

/**
 * Registry state message.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RegistryState implements SystemEvent {

    private Map<String, USID> registry;

    /**
     * Constructor.
     * @param registry Registry entries.
     */
    public RegistryState(final Map<String, USID> registry) {
        this.registry = new HashMap<>(registry);
    }

    @SuppressWarnings("unused")
    private RegistryState() {
    }

    /**
     * Returns the registry state.
     * @return registry state.
     */
    public Map<String, USID> getRegistry() {
        return registry;
    }

    @Override
    public String toString() {
        return "RegistryState{" + "registry=" + registry + '}';
    }
}
