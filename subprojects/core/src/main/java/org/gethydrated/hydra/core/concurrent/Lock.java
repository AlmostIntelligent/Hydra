package org.gethydrated.hydra.core.concurrent;

/**
 *
 */
public class Lock {

    private final String id;

    private final RequestType type;

    public Lock(String id, RequestType type) {
        this.type = type;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public RequestType getType() {
        return type;
    }

    public static enum RequestType{
        LOCK,
        UNLOCK
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lock lock = (Lock) o;

        if (id != null ? !id.equals(lock.id) : lock.id != null) return false;
        if (type != lock.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
