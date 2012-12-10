package org.gethydrated.hydra.actors;

import org.gethydrated.hydra.actors.dispatch.Dispatcher;
import org.gethydrated.hydra.actors.dispatch.Dispatchers;
import org.gethydrated.hydra.actors.event.EventStream;
import org.gethydrated.hydra.actors.event.SystemEventStream;
import org.gethydrated.hydra.actors.internal.RootGuardian;
import org.gethydrated.hydra.actors.internal.StandardActorFactory;
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
    private final ActorNode rootGuardian;

    /**
     * Application guardian.
     */
    private final ActorNode appGuardian;

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
        rootGuardian = new ActorNode("", new StandardActorFactory(RootGuardian.class), null, this);
        appGuardian = rootGuardian.getChildByName("app");
        eventStream.startEventHandling(1);
    }

    /**
     * Shuts down the actor system.
     */
    public void shutdown() {
        eventStream.stopEventHandling();
        rootGuardian.stop();
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
        return appGuardian.spawnActor(actorClass, name);
    }

    @Override
    public ActorRef spawnActor(final ActorFactory actorFactory, final String name) {
        return appGuardian.spawnActor(actorFactory, name);
    }

    @Override
    public ActorRef getActor(final String uri) {
        if (uri.startsWith("/")) {
            return rootGuardian.getActor(uri.substring(1));
        } else if (uri.startsWith("..")) {
            throw new RuntimeException("Actor not found.");
        } else {
            return rootGuardian.getActor(uri);
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
