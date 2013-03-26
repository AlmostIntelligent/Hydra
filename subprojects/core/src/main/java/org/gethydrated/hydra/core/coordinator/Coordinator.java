package org.gethydrated.hydra.core.coordinator;

import java.util.UUID;

/**
 *
 */
public interface Coordinator {

    boolean isLocal();

    void acquireLock(UUID uuid);

    void releaseLock(UUID uuid);

    void grantedLock();

    boolean hasLock();

    public interface Callback<T> {
        void call(T obj);
    }


}
