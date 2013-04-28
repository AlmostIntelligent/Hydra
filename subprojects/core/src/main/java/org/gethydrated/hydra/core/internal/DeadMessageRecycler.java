package org.gethydrated.hydra.core.internal;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.api.event.*;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.core.sid.DefaultSIDFactory;

/**
 * An actor that listens on dead messages and tries
 * to deliver the relevant messages like monitor or link.
 */
public class DeadMessageRecycler extends Actor {

    private DefaultSIDFactory sidFactory;

    public DeadMessageRecycler(DefaultSIDFactory sidFactory) {
        this.sidFactory = sidFactory;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof DeadMessage) {
            Object m = ((DeadMessage) message).getMessage();
            if (m instanceof Monitor) {
                SID sid = sidFactory.fromUSID(((Monitor) m).getSource());
                sid.tell(new ServiceDown(((Monitor) m).getTarget(),((Monitor) m).getSource(), "actor stopped."), null);
            }
            if (m instanceof Link) {
                SID sid = sidFactory.fromUSID(((Link)m).getSource());
                sid.tell(new ServiceExit(((Link) m).getTarget(), ((Link) m).getSource(), "actor stopped."), null);
            }
        }
    }

    @Override
    public void onStart() {
        getSystem().getEventStream().subscribe(getSelf(), DeadMessage.class);
    }

    @Override
    public void onStop() {
        getSystem().getEventStream().unsubscribe(getSelf());
    }
}
