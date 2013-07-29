package org.gethydrated.hydra.actors.actors;

import org.gethydrated.hydra.actors.ActorCreator;
import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.actors.SystemMessages.Stopped;
import org.gethydrated.hydra.actors.logging.LoggingAdapter;
import org.gethydrated.hydra.actors.refs.AbstractMinimalRef;
import org.gethydrated.hydra.actors.refs.InternalRef;
import org.gethydrated.hydra.actors.refs.NullRef;
import org.slf4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * Root guardian.
 * 
 * @author Christian Kulpa
 * @author Hanno Sternberg
 * @since 0.1.0
 */
public final class RootGuardian extends AbstractMinimalRef {

    private final Logger logger;

    private InternalRef appGuardian;

    private InternalRef sysGuardian;

    private final List<Runnable> terminationHooks = new LinkedList<>();

    private volatile boolean running = true;

    /**
     * Constructor.
     * @param system parent actor system.
     */
    public RootGuardian(final ActorSystem system) {
        logger = new LoggingAdapter(RootGuardian.class, system);
        logger.info("RootGuardian created.");
    }

    @Override
    public void stop() {
        appGuardian.stop();
    }

    @Override
    public synchronized void tellSystem(final Object o, final ActorRef sender) {
        if (o instanceof Stopped) {
            if (sysGuardian.equals(sender)) {
                running = false;
                logger.info("Root guardian stopped.");
                runTerminationHooks();
                logger.debug("Termination hooks done.");
            }
        }
    }

    /**
     * Sets the application guardian.
     * @param appGuardian application guardian.
     */
    public void setAppGuardian(final InternalRef appGuardian) {
        this.appGuardian = appGuardian;
    }

    /**
     * Sets the system guardian.
     * @param sysGuardian system guardian.
     */
    public void setSysGuardian(final InternalRef sysGuardian) {
        this.sysGuardian = sysGuardian;
    }

    @Override
    public InternalRef getChild(final String name) {
        if (name.equals("sys")) {
            return sysGuardian;
        } else if (name.equals("app")) {
            return appGuardian;
        }
        return new NullRef();
    }

    @Override
    public InternalRef findActor(final List<String> names) {
        if (names.isEmpty()) {
            return this;
        }
        final InternalRef ref = getChild(names.remove(0));
        return ref.findActor(names);
    }

    @Override
    public boolean isTerminated() {
        return !running;
    }

    @Override
    public ActorPath getPath() {
        return new ActorPath();
    }

    @Override
    public InternalRef getParent() {
        return new NullRef();
    }

    @Override
    public ActorCreator getCreator() {
        return null;
    }

    /**
     * Adds a new termination hook.
     * @param hook termination hook.
     */
    public void addTerminationHook(final Runnable hook) {
        if (running) {
            synchronized (terminationHooks) {
                terminationHooks.add(hook);
            }
        }
    }

    private void runTerminationHooks() {
        synchronized (terminationHooks) {
            for (final Runnable r : terminationHooks) {
                r.run();
            }
        }
    }
}
