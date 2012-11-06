package org.gethydrated.hydra.actors.node;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorFactory;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorSource;
import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.actors.ActorURI;
import org.gethydrated.hydra.actors.internal.ActorRefImpl;
import org.gethydrated.hydra.actors.mailbox.MailBox;
import org.gethydrated.hydra.actors.mailbox.Message;

/**
 * @author Christian Kulpa
 *
 */
public class ActorNode implements ActorSource, ActorContext {
    
	private final ActorSystem system;
	
	private final ActorNode parent;
	
	private final ActorFactory factory;
	
	private final String name;
	
	private final Map<String, ActorNode> children = new ConcurrentHashMap<>();
	
	private final ExecutorService threadpool = Executors.newSingleThreadExecutor();
	
	private Dispatcher dispatcher;
	
	private final MailBox mailbox = new MailBox();
	
	private Actor actor;
	
	private static ThreadLocal<ActorNode> nodeRef = new ThreadLocal<>();
	
	private boolean running = true;
	
	public ActorNode(String name, ActorFactory factory, ActorNode parent, ActorSystem system) {
		this.name = Objects.requireNonNull(name);
		this.factory = factory;
		this.parent = parent;
		this.system = system;
		start();
	}
	
	public void process(Message message) {
		try {
			actor.onReceive(message.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public ActorRef spawnActor(Class<? extends Actor> actorClass, String name) {
		return spawnActor(new StandardActorFactory(actorClass), name);
	}

	@Override
	public ActorRef spawnActor(ActorFactory actorFactory, String name) {
		return createChild(name, actorFactory).getRef();
	}

	@Override
	public ActorRef getActor(String uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActorRef getActor(ActorURI uri) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getName() {
		return name;
	}	
	
	public ActorRef getRef() {
		return new ActorRefImpl(this);
	}

	public ActorContext getContext() {
		return this;
	}
	
	public ActorSystem getSystem() {
		return system;
	}
	
	public MailBox getMailbox() {
		return mailbox;
	}
	
	public ActorNode getChildByName(String name) {
		return children.get(name);
	}
	
	private void start() {
		createActor();
		dispatcher = new Dispatcher(mailbox, this);
		threadpool.submit(dispatcher);
	}
	
	public void stop() {
		running = false;
	}
	
	public boolean isTerminated() {
		return !running;
	}
	
	private void createActor() {
		nodeRef.set(this);
		try {
			actor = factory.create();
			actor.onStart();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		nodeRef.remove();
	}
	
	private ActorNode createChild(String name, ActorFactory factory) {
		if (!children.containsKey(Objects.requireNonNull(name))) {
			ActorNode node = new ActorNode(name, Objects.requireNonNull(factory), this, system);
			children.put(name, node);
			return node;
		} else {
			throw new RuntimeException("Actorname '" + name + "' already in use");
		}
	}
	
	public static ActorNode getLocalActorNode() {
		return nodeRef.get();
	}

}
