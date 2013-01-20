package org.gethydrated.hydra.core.classloader;

import org.gethydrated.hydra.core.internal.Archive;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds all Classloaders for detected
 * hydra archives.
 */
public class ClassLoaders {

    Map<String, Archive> archives = new HashMap<>();

}
