package org.gethydrated.hydra.core.registry;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.core.messages.RegisterService;
import org.gethydrated.hydra.core.messages.ServiceDown;
import org.gethydrated.hydra.core.messages.UnregisterService;

import javax.naming.NameNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class LocalRegistry  extends Actor {

    private Map<String, SID> registry = new HashMap<>();

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof RegisterService) {
            register(((RegisterService) message).getSID(), ((RegisterService) message).getName());
        } else if(message instanceof UnregisterService) {
            unregister(((UnregisterService) message).getName());
        } else if(message instanceof String) {
            retrieve((String) message);
        } else if(message instanceof ServiceDown) {
            checkServiceDown((ServiceDown) message);
        }
    }

    private void checkServiceDown(ServiceDown serviceDown) {
        while (registry.containsValue(serviceDown)) {
            registry.values().remove(serviceDown);
        }
    }

    private void register(SID sid, String name) {
        if(registry.containsKey(name)) {
            getSender().tell("nameinuse", getSelf());
        } else {
            registry.put(name, sid);
            getSender().tell("ok", getSelf());
        }
    }

    private void unregister(String name) {
        registry.remove(name);
    }

    private void retrieve(String name) {
        SID sid = registry.get(name);
        if(sid != null) {
            getSender().tell(sid, getSelf());
        } else {
            getSender().tell(new NameNotFoundException(), getSelf());
        }
    }

    @Override
    public void onStart() {
        getSystem().getEventStream().subscribe(getSelf(), ServiceDown.class);
    }

    @Override
    public void onStop() {
        getSystem().getEventStream().unsubscribe(getSelf());
    }
}
