package org.gethydrated.hydra.actors.node;

import org.gethydrated.hydra.actors.*;
import org.gethydrated.hydra.actors.SystemMessages.*;
import org.gethydrated.hydra.actors.logging.LoggingAdapter;
import org.gethydrated.hydra.actors.mailbox.Mailbox;
import org.gethydrated.hydra.actors.mailbox.Message;
import org.gethydrated.hydra.api.util.Util;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.util.List;

/**
 * @author Christian Kulpa
 *
 */
public class ActorNode implements ActorSource, ActorContext {
    
	private final ActorSystem system;

    private final InternalRef self;

    private final InternalRef parent;
	
	private final ActorFactory factory;
	
	private final Logger logger;
	
	private final Children children;

    private final Watchers watchers;
	
	private final Mailbox mailbox;

    private Actor actor;

    private ActorRef sender = null;
	
	private static final ThreadLocal<ActorNode> nodeRef = new ThreadLocal<>();
	
	private ActorLifecyle status = ActorLifecyle.CREATED;
	
	public ActorNode(InternalRef self, InternalRef parent, ActorFactory factory, ActorSystem system) {
        logger = new LoggingAdapter(ActorNode.class, system);
		this.factory = factory;
        this.self = self;
        this.parent = parent;
		this.system = system;
        this.watchers = new Watchers(self, system.getEventStream());
        this.children = new Children(self, system);
        this.mailbox = system.getDefaultDispatcher().createMailbox(this); //TODO: get dispatchername from config
		logger.debug("ActorNode for actor: '" + self + "' created");
	}
	
	public synchronized void process(Message message) {
		getSystem().getClock().increment();
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

    public synchronized void processSystem(Message message) {
        if(status == ActorLifecyle.STOPPED) {
            return;
        }
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
            } else if (o instanceof Stopped) {
                childStopped(((Stopped)o).getPath());
            } else if(o instanceof Watch) {
                watchers.addWatcher(((Watch) o).getTarget());
            } else if (o instanceof UnWatch) {
                watchers.removeWatcher(((UnWatch) o).getTarget());
            } else if (o instanceof WatcheeStopped) {
                watchers.removeWatcher(((WatcheeStopped) o).getTarget());
                stop();
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
        try {
            ActorPath ap = ActorPath.apply(self.getPath(), uri);
            return getActor(ap);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

	@Override
	public ActorRef getActor(ActorPath path) {
		return system.getActor(path);
	}

    @Override
    public ActorRef getActor(List<String> names) {
        if(names.isEmpty()) {
            return self;
        }
        String target = names.remove(0);
        InternalRef ref = children.getChild(target);
        if (ref != null) {
            return ref.unwrap().getActor(names);
        }
        throw new RuntimeException("Actor not found:" + getPath().toString() + "/" + target);
    }

    @Override
    public List<String> getChildren() {
        return children.getAllChildren();
    }

    @Override
    public ActorRef getSelf() {
        return getRef();
    }

    @Override
	public String getName() {
		return self.getName();
	}

    public ActorPath getPath() {
        return self.getPath();
    }

    @Override
    public void watch(ActorRef target) {
        watchers.addWatched((InternalRef) target);
    }

    @Override
    public void unwatch(ActorRef target) {
        watchers.removeWatched((InternalRef) target);
    }

    @Override
    public void stop(ActorRef target) {
        ((InternalRef) target).tellSystem(new Stop(), self);
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
	
	private void start() {
        status = ActorLifecyle.STARTING;
		createActor();
        mailbox.setSuspended(false);
        status = ActorLifecyle.RUNNING;
	}
	
	private void stop() {
        logger.debug("Stopping actor '{}'", self);
        status = ActorLifecyle.STOPPING;
        mailbox.setSuspended(true);
		children.stopChildren();
        if(children.isEmpty()) {

            terminate();
        }
	}

    private void pause() {
        status = ActorLifecyle.SUSPENDED;
        mailbox.setSuspended(true);
    }

    private void resume() {
        status = ActorLifecyle.RUNNING;
        mailbox.setSuspended(false);
    }

    private void childStopped(ActorPath path) {
        children.removeChild(path);
        if(status == ActorLifecyle.RESTARTING || status == ActorLifecyle.STOPPING) {
            if(children.isEmpty()) {
                terminate();
            }
        }
    }

    private void terminate() {
        watchers.close();
        try {
            actor.onStop();
        } catch (Exception e) {
            logger.error("Error shutting down actor '{}':", self, e);
        }
        parent.tellSystem(new Stopped(self.getPath()), self);
        status = ActorLifecyle.STOPPED;
        mailbox.setScheduled(false);
        system.getDefaultDispatcher().closeMailbox(this);
        logger.info("Actor '{}' stopped.", self);
    }

	public boolean isTerminated() {
		return status == ActorLifecyle.STOPPED;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        if (o instanceof ActorPath) {
            System.out.println(self.getPath() + " - " + o.toString());
        }

        if (getClass() != o.getClass()) return false;

        ActorNode actorNode = (ActorNode) o;

        return getPath().equals(actorNode.getPath());

    }

    @Override
    public int hashCode() {
        return self.getPath().hashCode();
    }

	public static ActorNode getLocalActorNode() {
		return nodeRef.get();
	}

    public void attachChild(InternalRef actor) {
        children.attachChild(actor);
    }
}
