package org.gethydrated.hydra.actors.node;

import org.gethydrated.hydra.actors.*;
import org.gethydrated.hydra.actors.ActorContext;
import org.gethydrated.hydra.actors.SystemMessages.*;
import org.gethydrated.hydra.actors.dispatch.Dispatcher;
import org.gethydrated.hydra.actors.error.ActorInitialisationException;
import org.gethydrated.hydra.actors.logging.LoggingAdapter;
import org.gethydrated.hydra.actors.mailbox.Mailbox;
import org.gethydrated.hydra.actors.mailbox.Message;
import org.gethydrated.hydra.actors.refs.InternalRef;
import org.gethydrated.hydra.api.util.Util;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.util.List;

/**
 * @author Christian Kulpa
 *
 */
public class ActorNode implements ActorSource, ActorContext {

    private final InternalRef self;

    private final InternalRef parent;

    private final ActorFactory factory;

    private final ActorCreator creator;

    private final ActorSystem actorSystem;

    private final Dispatcher dispatcher;

    private final Mailbox mailbox;

    private final Children children;

    private final Watchers watchers;

    private final Supervisor supervisor;

    private final Logger logger;

    private Actor actor;

    private ActorRef sender;

    private static ThreadLocal<ActorNode> nodeRef = new ThreadLocal<>();
    private ActorLifecyle status = ActorLifecyle.CREATED;
    private boolean failed = false;

    public ActorNode(InternalRef self, InternalRef parent, ActorFactory actorFactory, ActorCreator creator) {
        this.self = self;
        this.parent = parent;
        this.factory = actorFactory;
        this.creator = creator;
        this.actorSystem = creator.getActorSystem();
        this.supervisor = new DefaultSupervisor(actorSystem);
        logger = new LoggingAdapter(ActorNode.class, actorSystem);
        dispatcher = actorSystem.getDefaultDispatcher();
        mailbox = dispatcher.createMailbox(this);
        mailbox.enqueueSystem(self, new Message(new Create(), self));
        children = new Children(self, creator);
        watchers = new Watchers(self, actorSystem.getEventStream());
    }

    public InternalRef getSelf() {
        return self;
    }

    public InternalRef getParent() {
        return parent;
    }

    public ActorRef getSender() {
        return sender;
    }

    public boolean isTerminated() {
        return (status == ActorLifecyle.STOPPED);
    }

    public ActorCreator getCreator() {
        return creator;
    }

    public ActorSystem getSystem() {
        return actorSystem;
    }

    @Override
    public void watch(ActorRef actor) {
        watchers.addWatched((InternalRef)actor);
    }

    @Override
    public void unwatch(ActorRef actor) {
        watchers.removeWatched((InternalRef)actor);
    }

    public Mailbox getMailbox() {
        return mailbox;
}

    public void start() {
        dispatcher.attach(this);
    }

    public void stop() {
        sendSystem(new Stop(), self);
    }

    public void restart(Throwable cause) {
        sendSystem(new Restart(cause), self);
    }

    public void suspend() {
        sendSystem(new Suspend(), self);
    }

    public void resume(Throwable cause) {
        sendSystem(new Restart(cause), self);
    }

    public void sendSystem(Object o, ActorRef sender) {
        try {
            dispatcher.dispatchSystem(this, new Message(o, sender));
        } catch (Exception e) {
            logger.error("Cough exception while sending message.", e);
        }
    }

    public void sendMessage(Object o, ActorRef sender) {
        try {
            dispatcher.dispatch(this, new Message(o, sender));
        } catch (Exception e) {
            logger.error("Cough exception while sending message.", e);
        }
    }

    public void handleMessage(Message message) {
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

    public void handleSystemMessage(Message m) {
        getSystem().getClock().increment();
        Object message = m.getMessage();
        try {
            if (message instanceof Create) {
                create();
            } else if (message instanceof Stop) {
                terminate();
            } else if (message instanceof Stopped) {
                childStopped(((Stopped)message).getPath());
            } else if(message instanceof Watch) {
                watchers.addWatcher(((Watch) message).getTarget());
            } else if (message instanceof UnWatch) {
                watchers.removeWatcher(((UnWatch) message).getTarget());
            } else if (message instanceof WatcheeStopped) {
                watchers.removeWatcher(((WatcheeStopped) message).getTarget());
                stop();
            } else if (message instanceof Failed) {
                handleFailedChild((Failed)message);
            }
        } catch (Exception e) {
            handleError(e);
        } catch (Throwable t) {
            if(!Util.isNonFatal(t))
                Util.throwUnchecked(t);
            handleError(t);
        }
    }

    private boolean handleInternal(Object o) {
        return false;
    }

    @Override
    public ActorRef spawnActor(Class<? extends Actor> actorClass, String name) {
        return spawnActor(new DefaultActorFactory(actorClass), name);
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
        return actorSystem.getActor(path);
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
        throw new RuntimeException("Actor not found:" + self.getPath().toString() + "/" + target);
    }

    @Override
    public void stopActor(ActorRef ref) {
        ((InternalRef)ref).stop();
    }

    @Override
    public List<ActorRef> getChildren() {
        return children.getAllChildren();
    }

    public InternalRef getChild(String name) {
        return children.getChild(name);
    }

    private void handleError(Throwable t) {
        if (!failed) {
            try {
                mailbox.suspend();
                children.suspendChildren();
                parent.tellSystem(new Failed(self, t), self);
                failed = true;
            } catch (Throwable th) {
                if (Util.isNonFatal(t)) {
                    logger.error("Exception in error handling: {}", th.getMessage(), th);
                    terminate();
                } else {
                    throw th;
                }
            }
        }
    }

    private void handleFailedChild(Failed failed) throws Throwable {
        supervisor.handleFailedChildren(failed.getCause(), failed.getChild());
    }

    private void create() throws Exception {
        try {
            nodeRef.set(this);
            actor = factory.create();
            actor.onStart();

            nodeRef.remove();
        } catch (Throwable e) {
            if(Util.isNonFatal(e)) {
                throw new ActorInitialisationException(e);
            }
            throw e;
        }
    }

    private void childStopped(ActorPath path) {
        children.removeChild(path);
        if(status == ActorLifecyle.RESTARTING || status == ActorLifecyle.STOPPING) {
            if(children.isEmpty()) {
                finishTerminate();
            }
        }
    }
    
    private void terminate() {
        logger.debug("Stopping actor '{}'", self);
        status = ActorLifecyle.STOPPING;
        children.stopChildren();
        if(children.isEmpty()) {
            finishTerminate();
        }
    }

    private void finishTerminate() {
        try {
            if (actor != null) {
                actor.onStop();
                actor = null;
            }
        } catch (Exception e) {
            logger.error("Error shutting down actor '{}':", self, e);
        }
        dispatcher.detach(this);
        watchers.close();
        status = ActorLifecyle.STOPPED;
        actor = null;
        logger.info("Actor '{}' stopped.", self);
        parent.tellSystem(new Stopped(self.getPath()), self);
    }

    public static ActorNode getLocalActorNode() {
        return nodeRef.get();
    }
}
