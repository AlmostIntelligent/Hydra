package org.gethydrated.hydra.actors.internal;

import java.util.Objects;
import java.util.concurrent.Future;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorURI;
import org.gethydrated.hydra.actors.mailbox.Message;
import org.gethydrated.hydra.actors.node.ActorNode;

public class ActorRefImpl implements ActorRef{
	
	public final ActorNode node;
	
	public ActorRefImpl(ActorNode node) {
		this.node = Objects.requireNonNull(node);
	}

	@Override
	public String getName() {
		return node.getName();
	}

	@Override
	public ActorURI getAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void tell(Object o, ActorRef sender) {
		node.getMailbox().put(new Message(o, sender));
	}

	@Override
	public void forward(Object o, ActorRef ref) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Future<?> ask(Object o) {
		// TODO Auto-generated method stub
		return null;
	}
}
