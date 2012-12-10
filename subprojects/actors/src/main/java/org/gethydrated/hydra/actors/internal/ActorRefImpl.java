package org.gethydrated.hydra.actors.internal;

import java.util.Objects;
import java.util.concurrent.Future;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorURI;
import org.gethydrated.hydra.actors.mailbox.Mailbox;
import org.gethydrated.hydra.actors.mailbox.Message;
import org.gethydrated.hydra.actors.node.ActorNode;

public class ActorRefImpl implements ActorRef{
	
	public final String name;

    public final Mailbox mailbox;
	
	public ActorRefImpl(ActorNode node) {
		Objects.requireNonNull(node);
        name = node.getName();
        mailbox = node.getMailbox();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ActorURI getAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void tell(Object o, ActorRef sender) {
		mailbox.offer(new Message(o, sender));
	}

	@Override
	public void forward(Message m) {
		mailbox.offer(m);
	}

	@Override()
	public Future<?> ask(Object o, ActorRef ref) {
		// TODO Auto-generated method stub
		return null;
	}
}
