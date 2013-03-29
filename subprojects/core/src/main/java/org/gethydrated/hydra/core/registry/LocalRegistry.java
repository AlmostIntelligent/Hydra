package org.gethydrated.hydra.core.registry;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.core.messages.Monitor;
import org.gethydrated.hydra.core.messages.ServiceDown;
import org.gethydrated.hydra.core.messages.UnMonitor;
import org.gethydrated.hydra.core.sid.DefaultSIDFactory;
import org.gethydrated.hydra.core.sid.InternalSID;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class LocalRegistry  extends Actor {

    private Map<String, SID> registry = new HashMap<>();

    private DefaultSIDFactory sidFactory;

    public LocalRegistry(DefaultSIDFactory sidFactory) {
        this.sidFactory = sidFactory;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        try {
        if(message instanceof RegisterService) {
            register(((RegisterService) message).getSID(), ((RegisterService) message).getName());
        } else if(message instanceof UnregisterService) {
            unregister(((UnregisterService) message).getName());
        } else if(message instanceof String) {
            retrieve((String) message);
        } else if(message instanceof ServiceDown) {
            checkServiceDown((ServiceDown) message);
        }
        }catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void checkServiceDown(ServiceDown serviceDown) {
        SID sid = sidFactory.fromUSID(serviceDown.getUsid());
        while (registry.containsValue(sid)) {
            registry.values().remove(sid);
        }
    }

    private void register(SID sid, String name) {
        if(registry.containsKey(name)) {
            getSender().tell(new RegistryException("Name is already in use."), getSelf());
        } else {
            if (((InternalSID) sid).getRef().isTerminated()) {
                getSender().tell(new RegistryException("Actor is already stopped"), getSelf());
            }
            registry.put(name, sid);
            SID self = sidFactory.fromActorRef(getSelf());
            sid.tell(new Monitor(self.getUSID()), self);
            if(getSender() != null) {
                getSender().tell("ok", getSelf());
            }
        }
    }

    private void unregister(String name) {
        SID ref = registry.remove(name);
        if(ref != null) {
            SID self = sidFactory.fromActorRef(getSelf());
            ref.tell(new UnMonitor(self.getUSID()), self);
            getSender().tell("ok", getSelf());
        } else {
            getSender().tell(new RegistryException("Name is not in use."), getSelf());
        }
    }

    private void retrieve(String name) {
        SID sid = registry.get(name);
        if(sid != null) {
            getSender().tell(sid, getSelf());
        } else {
            getSender().tell(new RegistryException("Name is not in use."), getSelf());
        }
    }
}
