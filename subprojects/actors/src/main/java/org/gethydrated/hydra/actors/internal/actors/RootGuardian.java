package org.gethydrated.hydra.actors.internal.actors;

import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.actors.SystemMessages.Stopped;
import org.gethydrated.hydra.actors.SystemMessages.Watch;
import org.gethydrated.hydra.actors.dispatch.Dispatchers;
import org.gethydrated.hydra.actors.internal.NodeRef;
import org.gethydrated.hydra.actors.internal.NodeRefImpl;
import org.gethydrated.hydra.actors.internal.StandardActorFactory;
import org.gethydrated.hydra.actors.logging.LoggingAdapter;
import org.gethydrated.hydra.actors.mailbox.Message;
import org.gethydrated.hydra.actors.node.ActorNode;
import org.slf4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

public class RootGuardian implements NodeRef {

    private final Logger logger;

    private NodeRef appGuardian;

    private NodeRef sysGuardian;

    private final List<Runnable> terminationHooks = new LinkedList<>();

    private volatile boolean running = true;

    public RootGuardian(ActorSystem actorSystem, Dispatchers dispatchers) {
        logger = new LoggingAdapter(RootGuardian.class, actorSystem);
        logger.info("RootGuardian created.");
        sysGuardian = new NodeRefImpl(new ActorPath().createChild("sys"), new StandardActorFactory(SysGuardian.class),this, actorSystem, dispatchers);
        appGuardian = new NodeRefImpl(new ActorPath().createChild("app"), new StandardActorFactory(AppGuardian.class),this, actorSystem, dispatchers);
        initGuardians();
    }

    @Override
    public void start() {
        logger.debug("Ignored rootGuardian call for 'start'.");
    }

    @Override
    public void stop() {
        appGuardian.stop();
    }

    @Override
    public void restart() {
        logger.debug("Ignored rootGuardian call for 'restart'.");
    }

    @Override
    public void pause() {
        logger.debug("Ignored rootGuardian call for 'pause'.");
    }

    @Override
    public void resume() {
        logger.debug("Ignored rootGuardian call for 'resume'");
    }

    @Override
    public void watch(ActorRef target) {
        logger.debug("Ignored rootGuardian call for 'watch'");
    }

    @Override
    public void unwatch(ActorRef target) {
        logger.debug("Ignored rootGuardian call for 'unwatch'");
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

    @Override
    public ActorNode unwrap() {
        throw new RuntimeException("Cannot unwrap rootGuardian.");
    }

    public boolean isTerminated() {
        return !running;
    }

    @Override
    public String getName() {
        return "rootGuardian";
    }

    @Override
    public ActorPath getPath() {
        return new ActorPath();
    }

    @Override
    public synchronized void tell(Object o, ActorRef sender) { }

    @Override
    public void forward(Message m) { }

    @Override
    public Future<?> ask(Object o) { return null; }

    public NodeRef getSystemGuardian() {
        return sysGuardian;
    }

    public NodeRef getAppGuardian() {
        return appGuardian;
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
        sysGuardian.start();
        appGuardian.start();
        appGuardian.tellSystem(new Watch(sysGuardian), sysGuardian);
        sysGuardian.tellSystem(new Watch(appGuardian), appGuardian);
    }
}
