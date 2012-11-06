package org.gethydrated.hydra.actors.node;

import java.util.concurrent.atomic.AtomicBoolean;

import org.gethydrated.hydra.actors.mailbox.MailBox;

public class Dispatcher implements Runnable {

	private AtomicBoolean running = new AtomicBoolean(true);
	
	private final MailBox mb;
	
	private final ActorNode an;
	
	public Dispatcher(MailBox mb, ActorNode an) {
		this.mb = mb;
		this.an = an;
	}
	
	@Override
	public void run() {
		while (running.get()) {
			try {
				an.process(mb.get());
			} catch (InterruptedException e) {
			}
		}
	}

	
	public void stop() {
		running.set(false);
	}
}
