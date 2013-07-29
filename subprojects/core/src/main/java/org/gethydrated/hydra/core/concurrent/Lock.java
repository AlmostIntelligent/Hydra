package org.gethydrated.hydra.core.concurrent;

/**
 * Lock object.
 */
public class Lock {

    private final String id;

    private final RequestType type;

    /**
     * Constructor.
     * @param id lock id
     * @param type lock request type.
     */
    public Lock(final String id, final RequestType type) {
        this.type = type;
        this.id = id;
    }

    /**
     * Returns the lock id.
     * @return lock id.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the lock request type.
     * @return request type.
     */
    public RequestType getType() {
        return type;
    }

    /**
     * Request type.
     * @author Christian Kulpa
     * @since 0.2.0
     */
    public static enum RequestType {
        /**
         * Lock.
         */
        LOCK,
        /**
         * Unlock.
         */
        UNLOCK
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lock lock = (Lock) o;

        if (id != null ? !id.equals(lock.id) : lock.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Lock{" +
                "id='" + id + '\'' +
                ", type=" + type +
                '}';
    }
}
