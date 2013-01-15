package org.gethydrated.hydra.actors.node;

import org.gethydrated.hydra.actors.*;
import org.gethydrated.hydra.actors.SystemMessages.*;
import org.gethydrated.hydra.actors.dispatch.Dispatchers;
import org.gethydrated.hydra.actors.internal.InternalRef;
import org.gethydrated.hydra.actors.internal.StandardActorFactory;
import org.gethydrated.hydra.actors.logging.LoggingAdapter;
import org.gethydrated.hydra.actors.mailbox.Mailbox;
import org.gethydrated.hydra.actors.mailbox.Message;
import org.gethydrated.hydra.api.util.Util;
import org.slf4j.Logger;

import java.util.Objects;

/**
 * @author Christian Kulpa
 *
 */
public class ActorNode implements ActorSource, ActorContext {
    
	private final ActorSystem system;

    private final InternalRef self;
	
	private final ActorFactory factory;
	
	private final ActorPath path;
	
	private final Logger logger;
	
	private final Children children;

    private final Watchers watchers;

    private final Dispatchers dispatchers;
	
	private final Mailbox mailbox;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        if (o instanceof ActorPath) {
            System.out.println(path + " - " + o.toString());
        }

        if (getClass() != o.getClass()) return false;

        ActorNode actorNode = (ActorNode) o;

        if (!path.equals(actorNode.path)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    private Actor actor;

    private ActorRef sender = null;
	
	private static final ThreadLocal<ActorNode> nodeRef = new ThreadLocal<>();
	
	private boolean running = true;
	
	public ActorNode(ActorPath path, ActorFactory factory, InternalRef self, ActorSystem system, Dispatchers dispatchers) {
        logger = new LoggingAdapter(ActorNode.class, system);
        this.path = Objects.requireNonNull(path);
		this.factory = factory;
        this.self = self;
		this.system = system;
        this.dispatchers = dispatchers;
        this.watchers = new Watchers(self, system.getEventStream());
        this.children = new Children(self, system, dispatchers);
        this.mailbox = dispatchers.lookupDispatcher("").createMailbox(this); //TODO: get dispatchername from config
		logger.debug("ActorNode for actor: '" +path+ "' created");
	}
	
	public void process(Message message) {
		try {
            if(!handleInternal(message.getMessage())) {
                sender = message.getSender();
			    actor.onReceive(message.getMessage());
                sender = null;
            }
        } catch (Exception e) {
            handleError(e);
		} catch (Throwable t) {
		    if(!Util.isNonFatal(t))
                throw t;
            handleError(t);
		}
	}

    public void processSystem(Message message) {
        try {
            Object o = message.getMessage();
            if(o instanceof Start) {
                start();
            } else if(o instanceof Stop) {
                stop();
            } else if (o instanceof Pause) {
                pause();
            } else if (o instanceof Resume) {
                resume();
            } else if(o instanceof Watch) {
                watchers.addWatcher(((Watch) o).getTarget());
            } else if (o instanceof UnWatch) {
                watchers.removeWatcher(((UnWatch) o).getTarget());
            }
        } catch (Exception e) {
            handleError(e);
        } catch (Throwable t) {
            if(!Util.isNonFatal(t))
                throw t;
            handleError(t);
        }
    }

    private boolean handleInternal(Object o) {
        return false;
    }

    private void handleError(Throwable t) {
        logger.error("Error processing message at '{}': {}", self, t );
        t.printStackTrace(System.err);
    }

    @Override
    public ActorRef getSender() {
        return sender;
    }

	@Override
	public ActorRef spawnActor(Class<? extends Actor> actorClass, String name) {
		return spawnActor(new StandardActorFactory(actorClass), name);
	}

	@Override
	public ActorRef spawnActor(ActorFactory actorFactory, String name) {
		return children.addChild(name, actorFactory);
	}

	@Override
	public ActorRef getActor(String uri) {
		/*if(uri.startsWith("/")) {
			return system.getActor(uri);
		} else if (uri.startsWith("../")) {
			if(self.parent() != null) {
				return self.parent().unwrap().getActor(uri.substring(3));
			} else {
				throw new RuntimeException("Actor not found.");
			}
		} else {
			int del = uri.indexOf('/');
			if(del == -1) {
				ActorRef n = children.get(uri);
				if(n != null) {
					return n;
				} else {
					throw new RuntimeException("Actor not found.");
				}
			} else {
				String child = uri.substring(0, del);
				String remain = uri.substring(del+1);
				InternalRef n = children.get(child);
				if(n != null) {
					return n.unwrap().getActor(remain);
				} else {
					throw new RuntimeException("Actor not found.");
				}
			}
		}    */return null;
	}

	@Override
	public ActorRef getActor(ActorPath path) {
		return getActor(path.toString());
	}
	
	@Override
	public String getName() {
		return path.getName();
	}

    public ActorPath getPath() {
        return path;
    }

    @Override
    public void watch(ActorRef target) {
        /*watchers.addWatched(target);
        if(target != self) {
            synchronized (watched) {
                if(!watched.contains(target)) {
                    watched.add(target);
                    ((InternalRef)target).tellSystem(new Watch(self), self);
                }
            }
        }       */
    }

    @Override
    public void unwatch(ActorRef target) {
        /*if(target != self) {
            synchronized (watched) {
                if(watched.contains(target)) {
                    watched.remove(target);
                    ((InternalRef)target).tellSystem(new UnWatch(self), self);
                }
            }
        }       */
    }

    @Override
    public void stop(ActorRef target) {
        ((InternalRef)target).tellSystem(new Stop(), self);
    }

    public ActorRef getRef() {
		return self;
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
	
	public InternalRef getChildByName(String name) {
		return children.getChild(name);
	}
	
	public void start() {
		createActor();
        mailbox.setSuspended(false);
	}
	
	private void stop() {
        System.out.println("Stopping actor " + path);
		running = false;
        mailbox.setSuspended(true);
		stopChildren();
		try {
		    actor.onStop();
        } catch (Exception e) {
            logger.error("Error shutting down actor ", e);
        }
        //self.parent().tellSystem(new Stopped(), self);
	}

    private void pause() {
        mailbox.setSuspended(true);
    }

    private void resume() {
        mailbox.setSuspended(false);
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
	
	private synchronized InternalRef createChild(String name, ActorFactory factory) {
	    /*if(running) {
            if(!children.containsKey(name)) {
                InternalRef node = new InternalRefImpl(path.createChild(name), Objects.requireNonNull(factory), self, system, dispatcher);
                try {
                    node.start();
                    children.put(name, node);
                } catch (Exception e) {
                    logger.error("Could not start actor '{}'", name, e);
                    throw e;
                }
                return node;
            } else {
                throw new RuntimeException("Actor name '" + name + "' already in use");
            }
	    } else {
	        throw new IllegalStateException("Actor not running");
	    }*/ return null;
	}
	
	private void stopChildren() {/*
        System.out.println(children.size());
        for(InternalRef n: children.values()) {
            System.out.println("Sending stop to actor: '" + n + "'");
            n.stop();
        }
        System.out.println("done");*/
	}

	public static ActorNode getLocalActorNode() {
		return nodeRef.get();
	}

}
