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

public class RootGuardian extends AbstractMinimalRef {

    private final Logger logger;

    private InternalRef appGuardian;

    private InternalRef sysGuardian;

    private final List<Runnable> terminationHooks = new LinkedList<>();

    private volatile boolean running = true;

    public RootGuardian(ActorSystem system) {
        logger = new LoggingAdapter(RootGuardian.class, system);
        logger.info("RootGuardian created.");
    }

    @Override
    public void stop() {
        appGuardian.stop();
    }

    @Override
    public synchronized void tellSystem(Object o, ActorRef sender) {
        if(o instanceof Stopped) {
            if(sysGuardian.equals(sender)) {
                running = false;
                logger.info("Root guardian stopped.");
                runTerminationHooks();
                logger.debug("Termination hooks done.");
            }
        }
    }

    public void setAppGuardian(InternalRef appGuardian) {
        this.appGuardian = appGuardian;
    }

    public void setSysGuardian(InternalRef sysGuardian) {
        this.sysGuardian = sysGuardian;
    }

    @Override
    public InternalRef getChild(String name) {
        if(name.equals("sys")) {
            return sysGuardian;
        } else if (name.equals("app")) {
            return appGuardian;
        }
        return new NullRef();
    }

    @Override
    public InternalRef findActor(List<String> names) {
        if(names.isEmpty()) {
            return this;
        }
        InternalRef ref = getChild(names.remove(0));
        return ref.findActor(names);
    }

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

    public void addTerminationHook(Runnable hook) {
        if(running) {
            synchronized (terminationHooks) {
                terminationHooks.add(hook);
            }
        }
    }

    public void runTerminationHooks() {
        synchronized (terminationHooks) {
            for(Runnable r : terminationHooks) {
                r.run();
            }
        }
    }

    private void initGuardians() {

    }
}
