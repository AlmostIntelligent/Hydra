package org.gethydrated.hydra.actors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.gethydrated.hydra.actors.SystemMessages.Watch;
import org.gethydrated.hydra.actors.actors.AppGuardian;
import org.gethydrated.hydra.actors.actors.RootGuardian;
import org.gethydrated.hydra.actors.actors.SysGuardian;
import org.gethydrated.hydra.actors.refs.ActorNodeRef;
import org.gethydrated.hydra.actors.refs.InternalRef;

/**
 *
 */
public class DefaultActorCreator implements ActorCreator {

    private RootGuardian root;

    private InternalRef appGuardian;

    private InternalRef sysGuardian;

    private final ActorSystem actorSystem;

    private final AtomicLong id = new AtomicLong(0);

    private final Map<ActorPath, InternalRef> mappings;

    public DefaultActorCreator(final ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
        init(actorSystem);
        mappings = new HashMap<>();
    }

    @Override
    public RootGuardian getRootGuardian() {
        return root;
    }

    @Override
    public InternalRef getSysGuardian() {
        return sysGuardian;
    }

    @Override
    public InternalRef getAppGuardian() {
        return appGuardian;
    }

    @Override
    public ActorPath createTempPath() {
        return new ActorPath().createChild("tmp").createChild(
                String.valueOf(id.incrementAndGet()));
    }

    @Override
    public void registerTempActor(final InternalRef actor, final ActorPath path) {
        if (!path.isChildOf(new ActorPath().createChild("tmp"))) {
            throw new RuntimeException(
                    "Temporary actor paths must be children of /tmp.");
        }
        synchronized (mappings) {
            if (!mappings.containsKey(path)) {
                mappings.put(path, actor);
            }
        }
    }

    @Override
    public void unregisterTempActor(final ActorPath path) {
        synchronized (mappings) {
            mappings.remove(path);
        }
    }

    @Override
    public ActorSystem getActorSystem() {
        return actorSystem;
    }

    @Override
    public ActorRef getTempActor(final List<String> names) {
        final ActorPath p = new ActorPath(
                names.toArray(new String[names.size()]));
        final ActorRef ref = mappings.get(p);
        if (ref != null) {
            return ref;
        }
        throw new RuntimeException("Actor not found:" + p.toString());
    }

    public void init(final ActorSystem actorSystem) {
        makeRootGuardian(actorSystem);
        makeSystemGuardian();
        makeAppGuardian();
        initGuardians();
    }

    private void makeRootGuardian(final ActorSystem actorSystem) {
        root = new RootGuardian(actorSystem);
    }

    private void makeSystemGuardian() {
        sysGuardian = new ActorNodeRef("sys", new DefaultActorFactory(
                SysGuardian.class), root, this);
        root.setSysGuardian(sysGuardian);
    }

    private void makeAppGuardian() {
        appGuardian = new ActorNodeRef("app", new DefaultActorFactory(
                AppGuardian.class), root, this);
        root.setAppGuardian(appGuardian);
    }

    private void initGuardians() {
        sysGuardian.start();
        appGuardian.start();
        appGuardian.tellSystem(new Watch(sysGuardian), sysGuardian);
        sysGuardian.tellSystem(new Watch(appGuardian), appGuardian);
    }
}
