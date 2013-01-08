package org.gethydrated.hydra.actors.internal.actors;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.actors.ActorURI;
import org.gethydrated.hydra.actors.internal.InternalRef;
import org.gethydrated.hydra.actors.logging.LoggingAdapter;
import org.gethydrated.hydra.actors.mailbox.Message;
import org.gethydrated.hydra.actors.node.ActorNode;
import org.gethydrated.hydra.actors.SystemMessages.*;
import org.slf4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;

public class RootGuardian implements InternalRef {

    private final Logger logger;

    private InternalRef appGuardian;

    private InternalRef sysGuardian;

    private final List<Runnable> terminationHooks = new LinkedList<>();

    private volatile boolean running = true;

    public RootGuardian(ActorSystem actorSystem) {
        logger = new LoggingAdapter(RootGuardian.class, actorSystem);
        logger.info("RootGuardian created.");
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
    public synchronized void tellSystem(Object o, ActorRef sender) {
        if(o instanceof Stopped) {
            if(sysGuardian.equals(sender)) {
                running = false;
                runTerminationHooks();
            }
        }
    }

    @Override
    public InternalRef parent() {
        return null;
    }

    @Override
    public ActorNode unwrap() {
        throw new RuntimeException("Cannot unwrap rootGuardian.");
    }

    @Override
    public boolean isTerminated() {
        return !running;
    }

    @Override
    public String getName() {
        return "rootGuardian";
    }

    @Override
    public ActorURI getAddress() {
        return null;
    }

    @Override
    public synchronized void tell(Object o, ActorRef sender) {
    }

    @Override
    public void forward(Message m) {
    }

    @Override
    public Future<?> ask(Object o) {
        return null; //TODO:
    }

    public void setSystemGuardian(InternalRef ref) {
        sysGuardian = ref;
    }

    public void setAppGuardian(InternalRef ref) {
        appGuardian = ref;
    }

    public void addTerminationHook(Runnable hook) {
        synchronized (terminationHooks) {
            terminationHooks.add(hook);
        }
    }

    public void runTerminationHooks() {
        synchronized (terminationHooks) {
            for(Runnable r : terminationHooks) {
                r.run();
            }
        }
    }
}
