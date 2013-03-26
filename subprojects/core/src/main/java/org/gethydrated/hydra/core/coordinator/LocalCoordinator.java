package org.gethydrated.hydra.core.coordinator;

import org.eclipse.jetty.util.ArrayQueue;

import java.util.Queue;
import java.util.UUID;

/**
 *
 */
public class LocalCoordinator implements Coordinator {

    private UUID lockHolder = null;

    private UUID localID;

    private Queue<UUID> queue = new ArrayQueue<>();

    private Callback<UUID> callback;

    public LocalCoordinator(UUID localUUID) {
        localID = localUUID;
    }

    @Override
    public boolean isLocal() {
        return true;
    }

    @Override
    public void acquireLock(UUID uuid) {
        if(lockHolder == null) {
            lockHolder = uuid;
            System.out.println("lockholder: " + lockHolder);
            sendGranted(uuid);
        } else {
            queue.add(uuid);
        }
    }

    @Override
    public void releaseLock(UUID uuid) {
        System.out.println("lockholder: " + lockHolder);
        System.out.println("uuid: " + uuid);
        if(lockHolder.equals(uuid)) {
            lockHolder = null;
            if(!queue.isEmpty()) {
                UUID newLockID = queue.remove();
                acquireLock(newLockID);
            }
        }
        if(queue.contains(uuid)) {
            queue.remove(uuid);
        }
    }

    @Override
    public void grantedLock() {

    }

    @Override
    public boolean hasLock() {
        return lockHolder.equals(localID);
    }

    public void setGrantedCallback(Callback<UUID> callback) {
        this.callback = callback;
    }

    private void sendGranted(UUID uuid) {
        if(callback == null) {
            throw new IllegalStateException("No uuid callback set.");
        }
        callback.call(uuid);
    }
}
