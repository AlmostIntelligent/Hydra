package org.gethydrated.hydra.actors;

import java.lang.Thread.UncaughtExceptionHandler;
import java.net.MalformedURLException;
import java.util.List;

import org.gethydrated.hydra.actors.clock.LamportsClock;
import org.gethydrated.hydra.actors.dispatch.Dispatcher;
import org.gethydrated.hydra.actors.dispatch.Dispatchers;
import org.gethydrated.hydra.actors.event.ActorEventStream;
import org.gethydrated.hydra.actors.event.SystemEventStream;
import org.gethydrated.hydra.actors.logging.FallbackLogger;
import org.gethydrated.hydra.actors.logging.LoggingAdapter;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.api.event.LogEvent;
import org.gethydrated.hydra.api.util.LogicalClock;
import org.gethydrated.hydra.api.util.Util;
import org.gethydrated.hydra.config.ConfigurationImpl;
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

    private final Configuration config;

    private final ActorCreator creator;

    /**
     * Lock object for threads awaiting system termination.
     */
    private final Object awaitLock = new Object();

    private final UncaughtExceptionHandler exceptionHandler = new UncaughtExceptionHandler() {

        @Override
        public void uncaughtException(final Thread t, final Throwable e) {
            e.printStackTrace(System.err);
            if (Util.isNonFatal(e)) {
                logger.error("Unhandled exception from thread '{}'",
                        t.getName(), e);
            } else {
                logger.error(
                        "Unhandled fatal error from thread '{}'. Shutting down ActorSystem.",
                        t.getName(), e);
            }

        }
    };

    private final Dispatchers dispatchers;

    private final Dispatcher defaultDispatcher;

    private final FallbackLogger fallbackLogger = new FallbackLogger();

    private final LogicalClock clock = new LamportsClock();

    /**
     * Private constructor.
     * 
     * @param cfg
     *            configuration.
     */
    private ActorSystem(final Configuration cfg) {
        eventStream.subscribe(fallbackLogger, LogEvent.class);
        logger.info("Creating actor system.");
        config = cfg;
        dispatchers = new Dispatchers(config, exceptionHandler);
        defaultDispatcher = dispatchers.lookupDispatcher("default-dispatcher");
        creator = new DefaultActorCreator(this);
        creator.getRootGuardian().addTerminationHook(new Runnable() {
            @Override
            public void run() {
                synchronized (awaitLock) {
                    logger.debug("shutdown");
                    awaitLock.notifyAll();
                }
            }
        });
    }

    /**
     * Shuts down the actor system.
     */
    public void shutdown() {
        logger.info("Stopping actor system.");
        creator.getAppGuardian().stop();
    }

    /**
     * Awaits the actor system termination.
     * 
     * @throws InterruptedException
     *             on interrupt while waiting.
     */
    public void await() throws InterruptedException {
        synchronized (awaitLock) {
            if (!isTerminated()) {
                awaitLock.wait();
            }
        }
    }

    /**
     * Returns if the actor system is terminated. If this method returns true it
     * will never return false again.
     * 
     * @return true when actor system is shut down, false otherwise.
     */
    public boolean isTerminated() {
        return creator.getRootGuardian().isTerminated();
    }

    /**
     * Returns the systems event stream.
     * 
     * @return System event stream.
     */
    public ActorEventStream getEventStream() {
        return eventStream;
    }

    public Dispatcher getDispatcher(final String name) {
        return dispatchers.lookupDispatcher(name);
    }

    public Dispatcher getDefaultDispatcher() {
        return defaultDispatcher;
    }

    public LogicalClock getClock() {
        return clock;
    }

    @Override
    public ActorRef spawnActor(final Class<? extends Actor> actorClass,
            final String name) {
        return creator.getAppGuardian().unwrap().spawnActor(actorClass, name);
    }

    @Override
    public ActorRef spawnActor(final ActorFactory actorFactory,
            final String name) {
        return creator.getAppGuardian().unwrap().spawnActor(actorFactory, name);
    }

    @Override
    public ActorRef getActor(final String uri) {
        try {
            final ActorPath ap = ActorPath.apply(new ActorPath(), uri);
            return getActor(ap);
        } catch (final MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ActorRef getActor(final ActorPath path) {
        return getActor(path.toList());
    }

    @Override
    public ActorRef getActor(final List<String> names) {
        if (names.get(0).equals("tmp")) {
            return creator.getTempActor(names);
        }
        return creator.getRootGuardian().findActor(names);
    }

    @Override
    public void stopActor(final ActorRef ref) {

    }

    /**
     * Creates a new actor system instance.
     * 
     * @return new actor system.
     */
    public static ActorSystem create() {
        final Configuration cfg = new ConfigurationImpl();
        return new ActorSystem(cfg);
    }

    public static ActorSystem create(final Configuration cfg) {
        return new ActorSystem(cfg);
    }
}
