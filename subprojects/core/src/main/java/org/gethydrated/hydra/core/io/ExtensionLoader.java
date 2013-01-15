package org.gethydrated.hydra.core.io;

/**
 * Loads all files from ext directory.
 * Java classes are visible to all services, but
 * no to Hydra itself.
 */
public class ExtensionLoader {

    public static ClassLoader loadExtDirectory() {
        return null;
    }
}
