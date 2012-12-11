package org.gethydrated.hydra.actors;

import org.gethydrated.hydra.actors.dispatch.Dispatcher;
import org.gethydrated.hydra.actors.dispatch.Dispatchers;
import org.gethydrated.hydra.actors.event.EventStream;
import org.gethydrated.hydra.actors.event.SystemEventStream;
import org.gethydrated.hydra.actors.internal.*;
import org.gethydrated.hydra.actors.logging.FallbackLogger;
import org.gethydrated.hydra.actors.logging.LoggingAdapter;
import org.gethydrated.hydra.actors.node.ActorNode;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Christian Kulpa
 */
public final class ActorSystem implements ActorSource {

    /**
     * Central event stream for this actor system.
     */
    private final SystemEventStream eventStream = new SystemEventStream();

    /**
     * Logger.
     */
    private final Logger logger = new LoggingAdapter(ActorSystem.class, this);

    /**
     * Root guardian.
     */
    private final InternalRef rootGuardian;

    /**
     * Application guardian.
     */
    private final InternalRef appGuardian;

    private final InternalRef sysGuardian;

    /**
     * Lock object for threads awaiting system termination.
     */
    private final Object awaitLock;

    private final Dispatcher defaultDispatcher = Dispatchers.newSharedDispatcher();

    /**
     * Private constructor.
     */
    private ActorSystem() {
        logger.info("Creating actor system.");
        awaitLock = new Object();
        rootGuardian = new RootGuardian(this);
        sysGuardian = new InternalRefImpl("sys", new StandardActorFactory(SysGuardian.class), rootGuardian, this, defaultDispatcher);
        sysGuardian.start();
        appGuardian = new InternalRefImpl("app", new StandardActorFactory(AppGuardian.class), rootGuardian, this, defaultDispatcher);
        appGuardian.start();
        eventStream.startEventHandling(1);
    }

    /**
     * Shuts down the actor system.
     */
    public void shutdown() {
        eventStream.stopEventHandling();
        rootGuardian.stop();
        //defaultDispatcher.shutdown();
        logger.info("Actor system stopped.");
        if (eventStream.hasRemainingEvents()) {
            FallbackLogger.log(eventStream.getRemainingEvents());
        }
        synchronized (awaitLock) {
            awaitLock.notifyAll();
        }
    }

    /**
     * Awaits the actor system termination.
     * @throws InterruptedException on interrupt while waiting.
     */
    public void await() throws InterruptedException {
        synchronized (awaitLock) {
            if (!isTerminated()) {
                awaitLock.wait();
            }
        }
    }

    /**
     * Returns if the actor system is terminated. If this method
     * returns true it will never return false again.
     * @return true when actor system is shut down, false otherwise.
     */
    public boolean isTerminated() {
        return rootGuardian.isTerminated();
    }

    /**
     * Returns the systems event stream.
     * @return System event stream.
     */
    public EventStream getEventStream() {
        return eventStream;
    }

    @Override
    public ActorRef spawnActor(final Class<? extends Actor> actorClass, final String name) {
        return appGuardian.unwrap().spawnActor(actorClass, name);
    }

    @Override
    public ActorRef spawnActor(final ActorFactory actorFactory, final String name) {
        return appGuardian.unwrap().spawnActor(actorFactory, name);
    }

    @Override
    public ActorRef getActor(final String uri) {
        if (uri.startsWith("/system/")) {
            return sysGuardian.unwrap().getActor(uri.substring(8));
        } else if (uri.startsWith("/app/")) {
            return appGuardian.unwrap().getActor(uri.substring(5));
        } else {
            throw new RuntimeException("Actor not found.");
        }
    }

    @Override
    public ActorRef getActor(final ActorURI uri) {
        return getActor(uri.toString());
    }

    /**
     * Creates a new actor system instance.
     * @return new actor system.
     */
    public static ActorSystem create() {
        return new ActorSystem();
    }
}
