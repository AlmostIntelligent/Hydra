package org.gethydrated.hydra.actors.cell;

import java.util.concurrent.ExecutorService;

import org.gethydrated.hydra.actors.ActorFactory;
import org.gethydrated.hydra.actors.mailbox.MailBox;

/**
 * @author Christian Kulpa
 *
 */
public class Cell {

	private MailBox mailBox;
	
	private ActorFactory actorFactory;
	
	private ExecutorService threadpool;
}
