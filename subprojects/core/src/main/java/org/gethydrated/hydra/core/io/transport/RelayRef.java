package org.gethydrated.hydra.core.io.transport;

import org.gethydrated.hydra.actors.ActorCreator;
import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.refs.AbstractMinimalRef;
import org.gethydrated.hydra.actors.refs.InternalRef;
import org.gethydrated.hydra.actors.refs.NullRef;
import org.gethydrated.hydra.api.service.USID;

import java.lang.ref.WeakReference;

/**
 * Temporary actor
 */
public class RelayRef extends AbstractMinimalRef {

    private WeakReference<ActorRef> parent;

    private ActorCreator creator;

    private ActorPath path;

    private USID relay;

    private boolean systemHint;

    public RelayRef(ActorRef parent, USID relay, boolean systemHint) {
        super();
        this.parent = new WeakReference<>(parent);
        this.creator = ((InternalRef) parent).getCreator();
        this.path = creator.createTempPath();
        this.relay = relay;
        this.systemHint = systemHint;
        creator.registerTempActor(this, path);
    }

    @Override
    public ActorPath getPath() {
        return path;
    }

    @Override
    public InternalRef getParent() {
        return new NullRef();
    }

    @Override
    public ActorCreator getCreator() {
        throw new RuntimeException("Operation not supported");
    }

    @Override
    public void tell(Object o, ActorRef sender) {
        ActorRef ref = parent.get();
        if (ref != null) {
            ref.tell(new RelayedMessage(o, sender, relay, systemHint), this);
        }
        creator.unregisterTempActor(path);
    }

    public static class RelayedMessage {
        private final Object message;
        private final ActorRef sender;
        private final USID relay;
        private final boolean systemHint;

        public RelayedMessage(Object message, ActorRef sender, USID relay, boolean systemHint) {
            this.message = message;
            this.sender = sender;
            this.relay = relay;
            this.systemHint = systemHint;
        }

        public USID getRelay() {
            return relay;
        }

        public Object getMessage() {
            return message;
        }

        public ActorRef getSender() {
            return sender;
        }

        public boolean isSystemRelay() {
            return systemHint;
        }
    }
}
