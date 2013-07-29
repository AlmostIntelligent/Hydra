package org.gethydrated.hydra.core.internal;

import org.jboss.modules.Module;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Hydra service archive.
 */
public final class Archive {

    private String name;

    private String version;

    private Module module;

    private final Map<String, Service> services = new HashMap<>();

    private final List<URL> archiveJars = new LinkedList<>();

    /**
     * Constructor.
     * @param name archive name.
     */
    public void setName(final String name) {
        if (this.name == null) {
            this.name = name;
        }
    }

    /**
     * Returns the archive name.
     * @return archive name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the archive version.
     * @param version version.
     */
    public void setVersion(final String version) {
        if (this.version == null) {
            this.version = version;
        }
    }

    @Override
    public String toString() {
        return "<Archive:" + name + ":" + version + " containing: " + services
                + ">";
    }

    /**
     * Adds a service to the archive.
     * @param service service.
     */
    public void addService(final Service service) {
        service.setArchive(this);
        services.put(service.getName(), service);
    }

    /**
     * Returns a service by the given name.
     * @param serviceName name of the service.
     * @return service.
     */
    public Service getService(final String serviceName) {
        return services.get(serviceName);
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public ClassLoader getClassLoader() {
        return module.getClassLoader();
    }
}
