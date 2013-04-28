package org.gethydrated.hydra.core.internal;

import java.net.URLClassLoader;

/**
 * Service holder.
 */
public class Service {
    private String name;
    private String activator;
    private Archive parent;

    /**
     * Sets the service name.
     * @param name service name.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns the service name.
     * @return service name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the service version.
     * @param version service version.
     */
    public void setVersion(final String version) {
    }

    /**
     * Adds a dependency to the service.
     * @param dep service depencency.
     */
    public void addDependency(final Dependency dep) {

    }

    /**
     * Sets the service activator.
     * @param activator service activator.
     */
    public void setActivator(final String activator) {
        this.activator = activator;
    }

    /**
     * Returns the service activator.
     * @return service activator.
     */
    public String getActivator() {
        return activator;
    }

    @Override
    public String toString() {
        return "<Service:" + name + ">";
    }

    /**
     * Sets the service archive.
     * @param archive serice archive.
     */
    public void setArchive(final Archive archive) {
        parent = archive;
    }

    /**
     * Creates a classloader for this service.
     * @return service classloader.
     */
    public ClassLoader getClassLoader() {
        return new URLClassLoader(parent.getArchiveURLs(), this.getClass()
                .getClassLoader().getParent());
    }
}
