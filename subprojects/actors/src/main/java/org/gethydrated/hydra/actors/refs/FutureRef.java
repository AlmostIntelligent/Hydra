package org.gethydrated.hydra.actors.refs;

import org.gethydrated.hydra.actors.ActorCreator;
import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.SyncVar;

import java.lang.ref.WeakReference;

/**
 * Temporary actor.
 */
public class FutureRef<T> extends AbstractMinimalRef {

    private WeakReference<SyncVar<T>> sync;

    private ActorPath path;

    private ActorCreator creator;

    public FutureRef(SyncVar<T> f, ActorCreator creator) {
        this.creator = creator;
        this.sync = new WeakReference<>(f);
        this.path = creator.createTempPath();
        creator.registerTempActor(this, path);
    }

    @Override
    public ActorPath getPath() {
        return path;
    }

    @Override
    public InternalRef getParent() {
        return new NullRef();
    }

    @Override
    public ActorCreator getCreator() {
        throw new RuntimeException("Operation not supported");
    }

    @Override
    public void tell(Object o, ActorRef ref) {
        SyncVar<T> var = sync.get();
        if(var != null) {
            if(o instanceof Throwable) {
                var.fail((Throwable) o);
            } else {
                try {
                    var.put((T) o);
                } catch (ClassCastException e) {
                    var.fail(e);
                }
            }
        }
        creator.unregisterTempActor(path);
    }
}
