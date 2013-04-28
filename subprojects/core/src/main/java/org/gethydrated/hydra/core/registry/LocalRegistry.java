package org.gethydrated.hydra.core.registry;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.api.event.Monitor;
import org.gethydrated.hydra.api.event.ServiceDown;
import org.gethydrated.hydra.api.event.UnMonitor;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.core.sid.DeadSID;
import org.gethydrated.hydra.core.sid.DefaultSIDFactory;
import org.gethydrated.hydra.core.sid.InternalSID;

import java.util.HashMap;
import java.util.Map;

/**
 * Local service registry. 
 */
public class LocalRegistry extends Actor {

    private final Map<String, SID> registry = new HashMap<>();

    private final DefaultSIDFactory sidFactory;

    /**
     * Constructor.
     * @param sidFactory service id factory.
     */
    public LocalRegistry(final DefaultSIDFactory sidFactory) {
        this.sidFactory = sidFactory;
    }

    @Override
    public void onReceive(final Object message) throws Exception {
        try {
            if (message instanceof RegisterService) {
                register(((RegisterService) message).getSID(),
                        ((RegisterService) message).getName());
            } else if (message instanceof UnregisterService) {
                unregister(((UnregisterService) message).getName());
            } else if (message instanceof String) {
                retrieve((String) message);
            } else if (message instanceof ServiceDown) {
                checkServiceDown((ServiceDown) message);
            }
        } catch (final Throwable t) {
            t.printStackTrace();
        }
    }

    private void checkServiceDown(final ServiceDown serviceDown) {
        final SID sid = new DeadSID(serviceDown.getSource());
        while (registry.containsValue(sid)) {
            registry.values().remove(sid);
        }
    }

    private void register(final SID sid, final String name) {
        if (registry.containsKey(name)) {
            getSender().tell(new RegistryException("Name is already in use."),
                    getSelf());
        } else {
            if (((InternalSID) sid).getRef().isTerminated()) {
                getSender().tell(
                        new RegistryException("Actor is already stopped"),
                        getSelf());
            }
            registry.put(name, sid);
            final SID self = sidFactory.fromActorRef(getSelf());
            sid.tell(new Monitor(self.getUSID(), sid.getUSID()), self);
            if (getSender() != null) {
                getSender().tell("ok", getSelf());
            }
        }
    }

    private void unregister(final String name) {
        final SID ref = registry.remove(name);
        if (ref != null) {
            final SID self = sidFactory.fromActorRef(getSelf());
            ref.tell(new UnMonitor(self.getUSID()), self);
            getSender().tell("ok", getSelf());
        } else {
            getSender().tell(new RegistryException("Name is not in use."),
                    getSelf());
        }
    }

    private void retrieve(final String name) {
        final SID sid = registry.get(name);
        if (sid != null) {
            getSender().tell(sid, getSelf());
        } else {
            getSender().tell(new RegistryException("Name is not in use."),
                    getSelf());
        }
    }
}
