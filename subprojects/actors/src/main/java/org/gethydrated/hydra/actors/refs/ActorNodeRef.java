package org.gethydrated.hydra.actors.refs;

import java.util.List;
import java.util.concurrent.Future;

import org.gethydrated.hydra.actors.ActorCreator;
import org.gethydrated.hydra.actors.ActorFactory;
import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.SyncVar;
import org.gethydrated.hydra.actors.node.ActorNode;

/**
 * Wraps an ActorNode into a Reference. There can only be one ActorNodeRef per
 * ActorNode.
 */
public class ActorNodeRef implements InternalRef {

    private final ActorNode actorNode;

    private final ActorPath actorPath;

    /**
     * Constructor.
     * @param name Name of the actor node.
     * @param actorFactory actor factory.
     * @param parent parent actor.
     * @param creator actor creator.
     */
    public ActorNodeRef(final String name, final ActorFactory actorFactory,
            final InternalRef parent, final ActorCreator creator) {
        this.actorPath = parent.getPath().createChild(name);
        this.actorNode = new ActorNode(this, parent, actorFactory, creator);
    }

    @Override
    public void start() {
        actorNode.start();
    }

    @Override
    public void stop() {
        actorNode.stop();
    }

    @Override
    public void restart(final Throwable cause) {
        actorNode.restart(cause);
    }

    @Override
    public void suspend() {
        actorNode.suspend();
    }

    @Override
    public void resume(final Throwable cause) {
        actorNode.resume(cause);
    }

    @Override
    public void tellSystem(final Object o, final ActorRef sender) {
        actorNode.sendSystem(o, sender);
    }

    @Override
    public InternalRef getChild(final String name) {
        final InternalRef child = actorNode.getChild(name);
        if (child != null) {
            return child;
        }
        return new NullRef();
    }

    @Override
    public InternalRef findActor(final List<String> names) {
        if (names.isEmpty()) {
            return this;
        }
        return getChild(names.remove(0)).findActor(names);
    }

    @Override
    public InternalRef getParent() {
        return actorNode.getParent();
    }

    @Override
    public ActorCreator getCreator() {
        return actorNode.getCreator();
    }

    @Override
    public ActorNode unwrap() {
        return actorNode;
    }

    @Override
    public String getName() {
        return actorPath.getName();
    }

    @Override
    public ActorPath getPath() {
        return actorPath;
    }

    @Override
    public void tell(final Object o, final ActorRef sender) {
        actorNode.sendMessage(o, sender);
    }

    @Override
    public Future<?> ask(final Object o) {
        final SyncVar<?> f = new SyncVar<>();
        final FutureRef<?> ref = new FutureRef<>(f, actorNode.getCreator());
        tell(o, ref);
        return f;
    }

    @Override
    public boolean isTerminated() {
        return actorNode.isTerminated();
    }

    @Override
    public String toString() {
        return actorPath.toString();
    }
}
