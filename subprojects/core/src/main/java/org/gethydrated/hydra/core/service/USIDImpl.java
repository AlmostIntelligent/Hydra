package org.gethydrated.hydra.core.service;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.FutureImpl;
import org.gethydrated.hydra.api.service.USID;

import java.util.concurrent.Future;

public class USIDImpl implements USID {

    ActorRef ref;

    public USIDImpl(ActorRef ref) {
        this.ref = ref;
    }

    @Override
    public void tell(Object message, USID sender) {
        if(sender == null) {
            ref.tell(message, null);
        } else {
            ref.tell(message, ((USIDImpl)sender).ref);
        }
    }

    @Override
    public Future<?> ask(Object message) {
        FutureImpl<Object> future = new FutureImpl<>();
        ref.tell(message, future);
        return future;
    }
}
