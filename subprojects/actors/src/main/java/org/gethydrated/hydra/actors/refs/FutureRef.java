package org.gethydrated.hydra.actors.refs;

import java.lang.ref.WeakReference;

import org.gethydrated.hydra.actors.ActorCreator;
import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.SyncVar;

/**
 * Temporary actor.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 * @param <T> Result type.
 */
public class FutureRef<T> extends AbstractMinimalRef {

    private final WeakReference<SyncVar<T>> sync;

    private final ActorPath path;

    private final ActorCreator creator;

    /**
     * Constructor.
     * @param f Result sync var.
     * @param creator temporary actor creator.
     */
    public FutureRef(final SyncVar<T> f, final ActorCreator creator) {
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

    @SuppressWarnings("unchecked")
    @Override
    public void tell(final Object o, final ActorRef ref) {
        final SyncVar<T> var = sync.get();
        if (var != null) {
            if (o instanceof Throwable) {
                var.fail((Throwable) o);
            } else {
                try {
                    var.put((T) o);
                } catch (final ClassCastException e) {
                    var.fail(e);
                }
            }
        }
        creator.unregisterTempActor(path);
    }
}
