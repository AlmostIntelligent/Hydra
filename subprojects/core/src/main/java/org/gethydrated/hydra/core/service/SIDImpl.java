package org.gethydrated.hydra.core.service;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.FutureImpl;
import org.gethydrated.hydra.api.service.SID;

import java.util.concurrent.Future;

public class SIDImpl implements SID {

    ActorRef ref;

    public SIDImpl(ActorRef ref) {
        this.ref = ref;
    }

    @Override
    public void tell(Object message, SID sender) {
        if(sender == null) {
            ref.tell(message, null);
        } else {
            ref.tell(message, ((SIDImpl)sender).ref);
        }
    }

    @Override
    public Future<?> ask(Object message) {
        FutureImpl<Object> future = new FutureImpl<>();
        ref.tell(message, future);
        return future;
    }
}
