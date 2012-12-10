package org.gethydrated.hydra.actors.node;

import java.util.concurrent.atomic.AtomicBoolean;

import org.gethydrated.hydra.actors.mailbox.BlockingQueueMailbox;
import org.gethydrated.hydra.actors.mailbox.Mailbox;

public class Dispatcher implements Runnable {

	private AtomicBoolean running = new AtomicBoolean(true);
	
	private final Mailbox mb;
	
	private final ActorNode an;
	
	public Dispatcher(Mailbox mb, ActorNode an) {
		this.mb = mb;
		this.an = an;
	}
	
	@Override
	public void run() {
		while (running.get()) {
			try {
				an.process(mb.take());
			} catch (InterruptedException e) {
			}
		}
	}

	
	public void stop() {
		running.set(false);
	}
}
