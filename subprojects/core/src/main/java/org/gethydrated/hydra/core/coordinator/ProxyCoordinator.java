package org.gethydrated.hydra.core.coordinator;

import java.util.UUID;

/**
 *
 */
public class ProxyCoordinator implements Coordinator{

    private Callback<UUID> grantedCallback;

    private Callback<UUID> acquireCallback;

    private Callback<UUID> releaseCallback;

    private UUID localId;

    private boolean lock = false;

    public ProxyCoordinator(UUID localId) {
        this.localId = localId;
    }

    public void setGrantedCallback(Callback<UUID> grantedCallback) {
        this.grantedCallback = grantedCallback;
    }

    public void setAcquireCallback(Callback<UUID> acquireCallback) {
        this.acquireCallback = acquireCallback;
    }

    public void setReleaseCallback(Callback<UUID> releaseCallback) {
        this.releaseCallback = releaseCallback;
    }

    @Override
    public boolean isLocal() {
        return false;
    }

    @Override
    public void acquireLock(UUID uuid) {
        if(uuid.equals(localId)) {
            acquireCallback.call(uuid);
        }
    }

    @Override
    public void releaseLock(UUID uuid) {
        if(uuid.equals(localId)) {
            releaseCallback.call(uuid);
            lock = false;
        }
    }

    @Override
    public void grantedLock() {
        lock = true;
        grantedCallback.call(localId);
    }

    @Override
    public boolean hasLock() {
        return lock;
    }
}
