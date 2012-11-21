package org.gethydrated.hydra.actors.logging;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.mailbox.MailBox;
import org.gethydrated.hydra.actors.mailbox.Message;
import org.gethydrated.hydra.actors.node.ActorNode;
import org.gethydrated.hydra.api.events.LogEvent;
import org.slf4j.LoggerFactory;

public class LogActor extends Actor {

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof LogEvent) {
			((LogEvent) message).logInto(LoggerFactory.getLogger(((LogEvent) message).getSource()));
		} 
	}

	@Override
	public void onStart() {
		getLogger(LogActor.class).info("Log actor startet.");
		getSystem().getEventStream().subscribe(getSelf(), LogEvent.class);
	}
	
	public void onStop() throws Exception {
	    //Hack until async shutdown
	    MailBox m = ((ActorNode)getContext()).getMailbox();
	    while(m.hasMessages()) {
	        Message me = m.get();
	        if(me != null) {
	            onReceive(me.getMessage());
	        }
	    }
	}
	
}
