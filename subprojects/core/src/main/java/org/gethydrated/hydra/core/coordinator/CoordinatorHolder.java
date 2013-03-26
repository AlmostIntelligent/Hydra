package org.gethydrated.hydra.core.coordinator;

import org.eclipse.jetty.util.ArrayQueue;

import java.util.Queue;
import java.util.UUID;

/**
 *
 */
public class CoordinatorHolder<T> implements Coordinator {

    private Coordinator delegate;

    private T currentLock;

    private Queue<T> queue = new ArrayQueue<>();

    private UUID localUUID;

    private Callback<T> grantedCallback;
    private Callback<T> releasedCallback;

    public CoordinatorHolder(UUID localUUID, Callback<UUID> globalCallback) {
        this.localUUID = localUUID;
        makeLocal(globalCallback);
    }

    public boolean isLocal() {
        return delegate.isLocal();
    }

    public void acquireLock(T lock) {
        if(lock == null) {
            return;
        }
        queue.add(lock);
        if(currentLock == null) {
            delegate.acquireLock(localUUID);
        }
    }

    public void releaseLock(T lock) {
        if(currentLock != null) {
            if(currentLock.equals(lock)) {
                currentLock = null;
                delegate.releaseLock(localUUID);
                if(!queue.isEmpty()) {
                    acquireLock(currentLock);
                }
                releasedCallback.call(lock);
            } else if(queue.contains(lock)) {
                queue.remove(lock);
                releasedCallback.call(lock);
            }
        }
    }

    public void setGrantedCallback(Callback<T> callback) {
        this.grantedCallback = callback;
    }

    public void setReleasedCallback(Callback<T> callback) {
        this.releasedCallback = callback;
    }

    @Override
    public void acquireLock(UUID uuid) {
        delegate.acquireLock(uuid);
    }

    @Override
    public void releaseLock(UUID uuid) {
        delegate.releaseLock(uuid);
    }

    @Override
    public void grantedLock() {
        delegate.grantedLock();
    }

    @Override
    public boolean hasLock() {
        return delegate.hasLock();
    }

    public void makeLocal(final Callback<UUID> globalCallback) {
        System.out.println("Coordinator id:" + localUUID);
        final LocalCoordinator lc = new LocalCoordinator(localUUID);
        lc.setGrantedCallback(new Callback<UUID>() {
            @Override
            public void call(UUID obj) {
                if(obj.equals(localUUID)) {
                    if(currentLock == null && !queue.isEmpty()) {
                        currentLock = queue.remove();
                        grantedCallback.call(currentLock);
                    }
                } else {
                    globalCallback.call(obj);
                }
            }
        });
        delegate = lc;
    }

    public void makeProxy(Callback<UUID> acquireCallback, Callback<UUID> releaseCallback) {
        ProxyCoordinator pc = new ProxyCoordinator(localUUID);
        pc.setGrantedCallback(new Callback<UUID>() {
            @Override
            public void call(UUID obj) {
                if(currentLock == null && !queue.isEmpty()) {
                    currentLock = queue.remove();
                    grantedCallback.call(currentLock);
                }
            }
        });
        pc.setAcquireCallback(acquireCallback);
        pc.setReleaseCallback(releaseCallback);
        delegate = pc;
    }
}
