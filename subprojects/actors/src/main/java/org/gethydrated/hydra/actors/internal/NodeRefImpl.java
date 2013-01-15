package org.gethydrated.hydra.actors.internal;

import org.gethydrated.hydra.actors.ActorFactory;
import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.actors.dispatch.Dispatchers;
import org.gethydrated.hydra.actors.mailbox.Mailbox;
import org.gethydrated.hydra.actors.node.ActorNode;

/**
 *
 */
public class NodeRefImpl extends InternalRefImpl implements NodeRef {

    private final ActorNode actorNode;

    public NodeRefImpl(ActorPath path, ActorFactory actorFactory, InternalRef parent, ActorSystem actorSystem, Dispatchers dispatchers) {
        super(path);
        actorNode = new ActorNode(path, actorFactory, this, actorSystem, dispatchers);
    }

    @Override
    public ActorNode unwrap() {
        return actorNode;
    }

    @Override
    protected Mailbox getMailbox() {
        return actorNode.getMailbox();
    }
}
