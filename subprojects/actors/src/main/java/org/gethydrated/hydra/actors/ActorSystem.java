package org.gethydrated.hydra.actors;

import org.gethydrated.hydra.actors.dispatch.Dispatcher;
import org.gethydrated.hydra.actors.dispatch.Dispatchers;
import org.gethydrated.hydra.actors.event.ActorEventStream;
import org.gethydrated.hydra.actors.event.SystemEventStream;
import org.gethydrated.hydra.actors.internal.actors.AppGuardian;
import org.gethydrated.hydra.actors.internal.InternalRef;
import org.gethydrated.hydra.actors.internal.InternalRefImpl;
import org.gethydrated.hydra.actors.internal.actors.RootGuardian;
import org.gethydrated.hydra.actors.internal.StandardActorFactory;
import org.gethydrated.hydra.actors.internal.actors.SysGuardian;
import org.gethydrated.hydra.actors.logging.FallbackLogger;
import org.gethydrated.hydra.actors.logging.LoggingAdapter;
import org.gethydrated.hydra.actors.SystemMessages.*;
import org.slf4j.Logger;


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
    private final RootGuardian rootGuardian;

    /**
     * Application guardian.
     */
    private final InternalRef appGuardian;

    /**
     * System guardian.
     */
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
        appGuardian = new InternalRefImpl("app", new StandardActorFactory(AppGuardian.class), rootGuardian, this, defaultDispatcher);
        initRootGuardians();
        rootGuardian.addTerminationHook(new Runnable() {
            @Override
            public void run() {
                eventStream.stopEventHandling();
                if (eventStream.hasRemainingEvents()) {
                    FallbackLogger.log(eventStream.getRemainingEvents());
                }
                synchronized (awaitLock) {
                    awaitLock.notifyAll();
                }
            }
        });
        eventStream.startEventHandling(1);
    }

    /**
     * Shuts down the actor system.
     */
    public void shutdown() {
        rootGuardian.stop();
        logger.info("Stopping actor system.");
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
    public ActorEventStream getEventStream() {
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

    private void initRootGuardians() {
        rootGuardian.setSystemGuardian(sysGuardian);
        rootGuardian.setAppGuardian(appGuardian);
        sysGuardian.start();
        appGuardian.start();
        appGuardian.tellSystem(new Watch(sysGuardian), sysGuardian);
        sysGuardian.tellSystem(new Watch(appGuardian), appGuardian);
    }

    /**
     * Creates a new actor system instance.
     * @return new actor system.
     */
    public static ActorSystem create() {
        return new ActorSystem();
    }
}
