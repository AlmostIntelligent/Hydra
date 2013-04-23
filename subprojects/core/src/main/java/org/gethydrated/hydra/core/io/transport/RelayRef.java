package org.gethydrated.hydra.core.io.transport;

import java.lang.ref.WeakReference;

import org.gethydrated.hydra.actors.ActorCreator;
import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.refs.AbstractMinimalRef;
import org.gethydrated.hydra.actors.refs.InternalRef;
import org.gethydrated.hydra.actors.refs.NullRef;
import org.gethydrated.hydra.api.service.USID;

/**
 * Temporary actor ref.
 * @author Christian Kulpa
 * @since 0.2.0
 */
public class RelayRef extends AbstractMinimalRef {

    private final WeakReference<ActorRef> parent;

    private final ActorCreator creator;

    private final ActorPath path;

    private final USID relay;

    private final boolean systemHint;

    /**
     * Constructor.
     * @param parent parent actor.
     * @param relay relay usid.
     * @param systemHint system relay.
     */
    public RelayRef(final ActorRef parent, final USID relay,
            final boolean systemHint) {
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
    public void tell(final Object o, final ActorRef sender) {
        final ActorRef ref = parent.get();
        if (ref != null) {
            ref.tell(new RelayedMessage(o, sender, relay, systemHint), this);
        }
        creator.unregisterTempActor(path);
    }

    /**
     * Relay message wrapper.
     * @author Christian Kulpa
     * @since 0.2.0
     */
    public static class RelayedMessage {
        private final Object message;
        private final ActorRef sender;
        private final USID relay;
        private final boolean systemHint;

        /**
         * Constructor.
         * @param message Message object.
         * @param sender Sender actor ref.
         * @param relay relay actor ref.
         * @param systemHint system relay.
         */
        public RelayedMessage(final Object message, final ActorRef sender,
                final USID relay, final boolean systemHint) {
            this.message = message;
            this.sender = sender;
            this.relay = relay;
            this.systemHint = systemHint;
        }

        /**
         * Returns the relay usid.
         * @return releay usid.
         */
        public USID getRelay() {
            return relay;
        }

        /**
         * Returns the message object.
         * @return message object.
         */
        public Object getMessage() {
            return message;
        }

        /**
         * Returns the message source.
         * @return message source.
         */
        public ActorRef getSender() {
            return sender;
        }

        /**
         * Returns if the relay is a system relay.
         * @return true if system relay.
         */
        public boolean isSystemRelay() {
            return systemHint;
        }
    }
}
