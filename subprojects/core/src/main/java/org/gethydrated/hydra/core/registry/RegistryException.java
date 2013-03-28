package org.gethydrated.hydra.core.registry;

/**
 *
 */
public class RegistryException extends Exception {
    public RegistryException(final Throwable e) {
        super(e);
    }

    public RegistryException(final String m) {
        super(m);
    }
}
