package org.gethydrated.hydra.actors.node;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorFactory;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorSource;
import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.actors.ActorURI;
import org.gethydrated.hydra.actors.internal.ActorRefImpl;
import org.gethydrated.hydra.actors.internal.StandardActorFactory;
import org.gethydrated.hydra.actors.logging.LoggingAdapter;
import org.gethydrated.hydra.actors.mailbox.BlockingQueueMailbox;
import org.gethydrated.hydra.actors.mailbox.Mailbox;
import org.gethydrated.hydra.actors.mailbox.Message;
import org.slf4j.Logger;

/**
 * @author Christian Kulpa
 *
 */
public class ActorNode implements ActorSource, ActorContext {
    
	private final ActorSystem system;
	
	private final ActorNode parent;
	
	private final ActorFactory factory;
	
	private final String name;
	
	private final Logger logger;
	
	private final ConcurrentMap<String, ActorNode> children = new ConcurrentHashMap<>();
	
	private final ExecutorService threadpool = Executors.newCachedThreadPool();
	
	private Dispatcher dispatcher;
	
	private final Mailbox mailbox = new BlockingQueueMailbox();

    private final Mailbox systemMailbox = new BlockingQueueMailbox();
	
	private Actor actor;
	
	private static ThreadLocal<ActorNode> nodeRef = new ThreadLocal<>();
	
	private boolean running = true;
	
	public ActorNode(String name, ActorFactory factory, ActorNode parent, ActorSystem system) {
		this.name = Objects.requireNonNull(name);
		this.factory = factory;
		this.parent = parent;
		this.system = system;
		logger = new LoggingAdapter(ActorNode.class, system);
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

    public void processSystem(Message message) {

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
		if(uri.startsWith("/")) {
			return system.getActor(uri);
		} else if (uri.startsWith("../")) {
			if(parent != null) {
				return parent.getActor(uri.substring(3));
			} else {
				throw new RuntimeException("Actor not found.");
			}
		} else {
			int del = uri.indexOf('/');
			if(del == -1) {
				ActorNode n = children.get(uri);
				if(n != null) {
					return n.getRef();
				} else {
					throw new RuntimeException("Actor not found.");
				}
			} else {
				String child = uri.substring(0, del);
				String remain = uri.substring(del+1);
				ActorNode n = children.get(child);
				if(n != null) {
					return n.getActor(remain);
				} else {
					throw new RuntimeException("Actor not found.");
				}
			}
		}
	}

	@Override
	public ActorRef getActor(ActorURI uri) {
		return getActor(uri.toString());
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
	
	public Mailbox getMailbox() {
		return mailbox;
	}
	
	public ExecutorService getExecutor() {
		return threadpool;
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
		dispatcher.stop();
		stopChildren();
		threadpool.shutdownNow();
		try {
		    actor.onStop();
        } catch (Exception e) {
            logger.error("Error shutting down actor ", e);
        }
		
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
	    if(running) {
    		ActorNode node = new ActorNode(name, Objects.requireNonNull(factory), this, system);
    		if(children.putIfAbsent(name, node) != null) {
    			throw new RuntimeException("Actor name '" + name + "' already in use");
    		}
    		return node;
	    } else {
	        throw new IllegalStateException("Actor not running");
	    }
	}
	
	private void stopChildren() {
	    for(ActorNode n : children.values()) {
	        n.stop();
	    }
	}
	
	public static ActorNode getLocalActorNode() {
		return nodeRef.get();
	}

}
